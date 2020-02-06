package com.realoldroot.nginx.zk;

/**
 * @author zhengenshen
 * @date 2019-02-26 15:29
 */
public interface ZkServerProvider extends RegistryService {

    /**
     * 注册到zk节点
     *
     * @throws Exception ex
     */
    void register() throws Exception;
}
