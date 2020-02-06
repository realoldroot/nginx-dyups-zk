package com.realoldroot.nginx.zk;

import java.io.Serializable;

/**
 * zk节点保存的数据
 *
 * @author zhengenshen
 * @date 2019-02-26 15:08
 */
public class ZkNodeData implements Serializable {

    private String host;

    private int port;

    private String serverName;

    private long startTime;


    public String getHost() {
        return host;
    }

    public ZkNodeData setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ZkNodeData setPort(int port) {
        this.port = port;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public ZkNodeData setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public ZkNodeData setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }
}
