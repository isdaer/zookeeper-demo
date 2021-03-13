package com.isdaer;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributeClient {

    private String connectString = "localhost:2181,localhost:2182,localhost:2183";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributeClient client = new DistributeClient();
        //1.获取ZK集群连接
        client.getConnect();
        //2.注册监听
        client.getChildren();
        //3.业务逻辑
        client.business();
    }

    //连接
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent event) {
                try {
                    getChildren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取子节点
    public void getChildren() throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren("/servers", true);

        //存储服务器节点主机名称集合
        ArrayList<String> hosts = new ArrayList<String>();

        for (String child : children) {
            //第一个参数为路径,第二个参数为监听(连接时已经存在一个监听,此处false),第三个参数为状态
            byte[] data = zkClient.getData("/servers/" + child, false, null);

            hosts.add(new String(data));
        }

        //将所有在线主机名称打印
        System.out.println(hosts);
    }

    //业务逻辑
    //使用sleep保证任务不直接结束
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}
