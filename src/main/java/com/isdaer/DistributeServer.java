package com.isdaer;

import org.apache.zookeeper.*;

import java.io.IOException;

public class DistributeServer {

    private String connectString = "localhost:2181,localhost:2182,localhost:2183";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeServer server = new DistributeServer();
        //1.连接ZK集群
        server.getConnect();
        //2.注册节点
        server.regist(args[0]);
        //3.业务代码
        server.business();
    }

    //连接
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });
    }

    //注册
    private void regist(String hostname) throws KeeperException, InterruptedException {
        //节点类型选择临时带序号节点 CreateMode.EPHEMERAL_SEQUENTIAL
        //临时保证节点下线自动删除
        //带序号保证不会重复
        String path = zkClient.create(
                "/servers/server", hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname + "节点已上线");
    }

    //业务逻辑
    //使用sleep保证任务不直接结束
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}
