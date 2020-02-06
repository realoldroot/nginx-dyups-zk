package com.realoldroot.nginx.zk;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

/**
 * @author zhengenshen
 * @date 2019-02-26 15:23
 */
public interface ZkServiceConsumer extends RegistryService {

    /**
     * 监听节点
     *
     * @param listener 监听器
     */
    PathChildrenCache watch(PathChildrenCacheListener listener);

    /**
     * 监听节点
     *
     * @param listener 监听器
     * @throws Exception ex
     */
    NodeCache watch(NodeCacheListener listener) throws Exception;

}
