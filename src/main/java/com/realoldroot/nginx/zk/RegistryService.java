package com.realoldroot.nginx.zk;

/**
 * zk 注册接口
 *
 * @author zhengenshen
 * @date 2019-02-26 14:53
 */
public interface RegistryService {


    /**
     * 初始化节点信息，创建客户端，连接到zk
     */
    void connect();

    /**
     * 关闭客户端
     */
    void close();

}
