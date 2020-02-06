package com.realoldroot.nginx;

import com.realoldroot.nginx.server.HttpServerRegistryServiceImpl;
import com.realoldroot.nginx.zk.ZkNodeData;
import com.realoldroot.nginx.zk.ZkServerProvider;

public class ServerTest {

    public static void main(String[] args) throws Exception {

        ZkNodeData data1 = new ZkNodeData();
        data1.setHost("localhost");
        data1.setPort(8181);
        data1.setServerName("server");
        data1.setStartTime(System.currentTimeMillis());
        ZkServerProvider service1 = new HttpServerRegistryServiceImpl("localhost", 2181, "http1", data1);
        service1.register();

        ZkNodeData data2 = new ZkNodeData();
        data2.setHost("localhost");
        data2.setPort(8181);
        data2.setServerName("server");
        data2.setStartTime(System.currentTimeMillis());
        ZkServerProvider service2 = new HttpServerRegistryServiceImpl("localhost", 2181, "http2", data2);
        service1.register();

    }
}
