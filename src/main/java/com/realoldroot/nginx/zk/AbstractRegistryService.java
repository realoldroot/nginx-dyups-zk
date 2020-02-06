package com.realoldroot.nginx.zk;

import com.realoldroot.nginx.util.JsonUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author zhengenshen
 * @date 2019-02-26 14:26
 */
public abstract class AbstractRegistryService implements ZkServerProvider, ZkServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistryService.class);

    protected CuratorFramework client;

    private String zkHost;

    private int zkPort;

    private ZkNodeData data;

    private WatchedEvent event;

    /**
     * 重试策略
     */
    private RetryPolicy retryPolicy;


    private ExecutorService executor;


    /**
     * 构造函数
     *
     * @param zkHost     zk地址
     * @param zkPort     zk端口号
     */
    public AbstractRegistryService(String zkHost, int zkPort) {
        this.zkHost = zkHost;
        this.zkPort = zkPort;
        this.retryPolicy = new ExponentialBackoffRetry(1000, 3);
    }


    /**
     * 注册根节点名称，格式为"/root"
     *
     * @return 根节点名称
     */
    protected abstract String getRoot();

    /**
     * 注册子节点名称，格式为"/subNode"
     *
     * @return 子节点名称
     */
    protected abstract String getSubNode();


    @Override
    public void connect() {
        data = initNodeData();
        client = CuratorFrameworkFactory.newClient(getConnectString(), retryPolicy);
        client.start();
    }

    /**
     * 注册到zk节点
     *
     * @throws Exception e
     */
    @Override
    public void register() throws Exception {
        if (client == null) {
            connect();
        }

        byte[] bytes = JsonUtil.writeValueAsBytes(data);

        String path = getRoot() + "/" + getSubNode();

        Stat stat = client.checkExists().forPath(path);
        if (stat == null) {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, bytes);
        } else {
            client.setData().forPath(path, bytes);
        }
        LOGGER.info("zk node register successful {}  {}", path, data);

    }

    /**
     * 初始化节点需要保存的信息
     *
     * @return 节点数据
     */
    protected abstract ZkNodeData initNodeData();

    @Override
    public PathChildrenCache watch(PathChildrenCacheListener listener) {
        if (client == null) {
            connect();
        }
        PathChildrenCache childrenCache = new PathChildrenCache(client, getRoot(), true);
        childrenCache.getListenable().addListener(listener);
        return childrenCache;
    }

    @Override
    public NodeCache watch(NodeCacheListener listener) {
        if (client == null) {
            connect();
        }
        NodeCache nodeCache = new NodeCache(client, getRoot() + "/" + getSubNode());
        nodeCache.getListenable().addListener(listener);
        return nodeCache;
    }

    /**
     * 关闭
     */
    @Override
    public void close() {
        client.close();
    }


    private String getConnectString() {
        return zkHost + ":" + zkPort;
    }

}
