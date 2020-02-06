package com.realoldroot.nginx;

import com.realoldroot.nginx.util.ConstDefine;
import com.realoldroot.nginx.util.JsonUtil;
import com.realoldroot.nginx.zk.AbstractRegistryService;
import com.realoldroot.nginx.zk.ZkNodeData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class NginxServiceConsumer extends AbstractRegistryService {


    private static final Logger LOGGER = LoggerFactory.getLogger(NginxServiceConsumer.class);

    /**
     * 保存了 服务名 -> 服务地址列表的 map
     */
    private Map<String, Set<String>> map = new HashMap<>();

    public NginxServiceConsumer(String zkHost, int zkPort) {
        super(zkHost, zkPort);
    }

    @Override
    protected String getRoot() {
        return ConstDefine.HTTP_SERVER_NODE;
    }

    @Override
    protected String getSubNode() {
        return "nginx";
    }

    /**
     * 注册到zk的数据，此类为服务消费方，不需要提供自己的数据供别人使用
     *
     * @return zkNodeData
     */
    @Override
    protected ZkNodeData initNodeData() {
        ZkNodeData data = new ZkNodeData();
        data.setHost("localhost");
        data.setPort(9999);
        data.setServerName("nginx");
        data.setStartTime(System.currentTimeMillis());
        return data;
    }

    /**
     * 启动方法
     * 先去注册中心查询一边是否有已存在的节点信息，然后更新到nginx中
     * <p>
     * 之后注册节点监听器
     *
     * @throws Exception e
     */
    public void start() throws Exception {
        try {
            List<String> childPaths = client.getChildren().forPath(getRoot());
            for (String childPath : childPaths) {
                String absPath = getRoot() + "/" + childPath;
                byte[] bytes = client.getData().forPath(absPath);
                if (bytes == null) continue;
                ZkNodeData nodeData;
                try {
                    nodeData = JsonUtil.readBytes(bytes, ZkNodeData.class);
                    String key = nodeData.getServerName();
                    Set<String> values = map.computeIfAbsent(key, k -> new HashSet<>());
                    values.add(build(nodeData));
                } catch (Exception e) {
                    LOGGER.error("node data parse error. path : {}", absPath);
                    continue;
                }
                LOGGER.info("node path : {} , data : {}", absPath, nodeData);
            }
            for (Entry<String, Set<String>> entry : map.entrySet()) {
                update(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        NginxPathChildrenListener listener = new NginxPathChildrenListener(this);
        PathChildrenCache childrenCache = this.watch(listener);
        childrenCache.start();
    }

    /**
     * 添加新增服务数据 并且更新到nginx中
     *
     * @param nodeData 新增节点数据
     */
    void add(ZkNodeData nodeData) {
        String key = nodeData.getServerName();
        Set<String> values = map.computeIfAbsent(key, k -> new HashSet<>());
        if (values.add(build(nodeData))) {
            update(key, values);
        }
    }

    /**
     * 子节点移除之后 更新服务数据，更新到nginx中
     *
     * @param nodeData 删除的节点数据
     */
    void remove(ZkNodeData nodeData) {
        String key = nodeData.getServerName();
        Set<String> values = map.computeIfAbsent(key, k -> new HashSet<>());
        if (values.remove(build(nodeData))) {
            update(key, values);
        }
    }


    /**
     * nginx dyups api 新增服务的结构数据
     *
     * @param nodeData 节点数据
     * @return str
     */
    private String build(ZkNodeData nodeData) {
        return "server " + nodeData.getHost() + ":" + nodeData.getPort() + ";";
    }


    /**
     * 更新nginx upstream数据
     * <p>
     * 如果set为空，则删除节点
     * <p>
     * 否则更新
     *
     * @param servername servername
     * @param servers    servers
     */
    private void update(String servername, Set<String> servers) {
        if (servers.isEmpty()) {
            map.remove(servername);
            NginxUpstreamClient.remove(servername);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String server : servers) {
                sb.append(server);
            }
            NginxUpstreamClient.update(servername, sb.toString());
        }
    }
}
