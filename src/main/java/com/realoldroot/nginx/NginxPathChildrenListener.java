package com.realoldroot.nginx;

import com.realoldroot.nginx.util.JsonUtil;
import com.realoldroot.nginx.zk.ZkNodeData;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * 子节点监听器，用来监听节点发生变化，及时更新到nginx
 */
public class NginxPathChildrenListener implements PathChildrenCacheListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(NginxPathChildrenListener.class);

    /**
     * 存放服务器地址
     */
    private NginxServiceConsumer consumer;

    public NginxPathChildrenListener(NginxServiceConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
        ChildData data = event.getData();
        if (data == null) {
            LOGGER.info("event type : {}", event.getType());
            return;
        }
        String path = data.getPath();
        byte[] bytes = data.getData();
        if (bytes == null) {
            return;
        }
        String serverName;
        Set<String> servers;
        ZkNodeData nodeData;
        try {
            nodeData = JsonUtil.readBytes(bytes, ZkNodeData.class);
        } catch (Exception e) {
            LOGGER.error("node data parse error. path : {}", path);
            return;
        }
        LOGGER.info("zk chileNode path : {} , event type : {}", path, event.getType());
        switch (event.getType()) {
            case CHILD_ADDED:
            case CHILD_UPDATED:
                consumer.add(nodeData);
                break;
            case CHILD_REMOVED:
                consumer.remove(nodeData);
                break;
            default:
                break;
        }

    }
}
