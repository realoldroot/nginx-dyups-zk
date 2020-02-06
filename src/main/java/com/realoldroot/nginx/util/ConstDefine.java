package com.realoldroot.nginx.util;

public class ConstDefine {

    /**
     * zk 根节点
     */
    private static final String ROOT = "/root";

    /**
     * http 服务注册节点
     */
    public static final String HTTP_SERVER_NODE = ROOT + "/httpServer";


    /**
     * nginx ngx_http_dyups_module api
     * 更新节点，粗颗粒度更新，覆盖更新
     */
    public static final String URL = "http://localhost:8081/upstream";

}
