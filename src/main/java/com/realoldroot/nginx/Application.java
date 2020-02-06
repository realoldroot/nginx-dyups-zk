package com.realoldroot.nginx;

public class Application {

    public static void main(String[] args) throws Exception {
        NginxServiceConsumer server = new NginxServiceConsumer("localhost", 2181);
        server.start();
        Thread.currentThread().join();
    }
}
