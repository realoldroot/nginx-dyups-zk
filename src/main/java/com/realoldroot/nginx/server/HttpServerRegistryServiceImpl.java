package com.realoldroot.nginx.server;

import com.realoldroot.nginx.zk.AbstractRegistryService;
import com.realoldroot.nginx.util.ConstDefine;
import com.realoldroot.nginx.zk.ZkNodeData;

public class HttpServerRegistryServiceImpl extends AbstractRegistryService {

    private String subNode;


    private ZkNodeData nodeData;

    /**
     * @param zkHost  zk地址
     * @param zkPort  zk端口
     * @param subNode 注册到子节点的path
     */
    public HttpServerRegistryServiceImpl(String zkHost, int zkPort, String subNode, ZkNodeData zkNodeData) {
        super(zkHost, zkPort);
        this.subNode = subNode;
    }

    @Override
    protected String getRoot() {
        return ConstDefine.HTTP_SERVER_NODE;
    }

    /**
     * 注册子节点path，每一个服务都不能重复
     *
     * @return subNode
     */
    @Override
    protected String getSubNode() {
        return subNode;
    }

    /**
     * 初始化节点信息，像zk注册节点，并保存节点数据
     * <p>
     * host : nginx upstream 转发的地址
     * <p>
     * port : nginx upstream 转发端口号
     * <p>
     * serverName : nginx upstream upsName，根据这个名称把当前服务加入到同一组upstream中
     *
     * @return nodeData
     */
    @Override
    protected ZkNodeData initNodeData() {

        return nodeData;
    }


}
