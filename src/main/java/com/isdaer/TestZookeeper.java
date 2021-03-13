package com.isdaer;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestZookeeper {

    private String connectString = "localhost:2181,localhost:2182,localhost:2183";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    //测试连接
    @Before
    public void init() throws IOException {
        /**
         * connectString:ZK服务器列表
         * sessionTimeout:会话超时时间(单位毫秒)
         * Watcher:Watcher事件通知处理器
         */
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
//
//                List<String> children = null;
//                try {
//                    children = zkClient.getChildren("/", false);
//                } catch (KeeperException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                for (String child : children) {
//                    System.out.println(child);
//                }
            }
        });
    }

    //测试创建
    @Test
    public void createNode() throws KeeperException, InterruptedException {
        /**
         * 参数1:ZK文件目录
         * 参数2:新增数据内容,要转换成字节数组
         * 参数3:权限控制
         *       Ids.OPEN_ACL_UNSAFE:完全开放的ACL，任何连接的客户端都可以操作该属性ZNode
         *       Ids.CREATOR_ALL_ACL:只有创建者才有ACL权限
         *       Ids.READ_ACL_UNSAFE:只能读取ACL
         * 参数4:数据类型
         *       CreateMode.PERSISTENT:持久型
         *       CreateModePERSISTENT_SEQUENTIAL:持久顺序型
         *       CreateMode.EPHEMERAL:临时型
         *       CreateMode.EPHEMERAL_SEQUENTIAL:临时顺序型
         */
        String path = zkClient.create("/jiedian", "xinzengshuju".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
    }

    //测试获取子节点
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {
        /**
         * 参数1:ZK路径
         * 参数2:是否监听子节点变化,true or false
         */
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    //判断节点是否存在
    @Test
    public void exist() throws KeeperException, InterruptedException {
        Stat exist1 = zkClient.exists("/jiedian", false);
        System.out.println("存在节点打印效果:" + exist1);
        Stat exist2 = zkClient.exists("/bucunzai", false);
        System.out.println("不存在节点打印效果:" + exist2);
    }
}
