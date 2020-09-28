package com.lagou.zk.demo;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.client.ZooKeeperSaslClient;

import java.util.List;

/*
演示zkClient如何使用监听器
 */
public class Get_Child_Change {
    public static void main(String[] args) throws InterruptedException {
        //获取到zkClient
        final ZkClient zkClient = new ZkClient("linux121:2181");

        //zkClient对指定目录进行监听(不存在目录:/lg-client)，指定收到通知之后的逻辑

        //对/lag-client注册了监听器，监听器是一直监听
        zkClient.subscribeChildChanges("/lg-client", new IZkChildListener() {
            //该方法是接收到通知之后的执行逻辑定义
            public void handleChildChange(String path, List<String> childs) throws Exception {
                //打印节点信息
                System.out.println(path + " childs changes ,current childs " + childs);
            }
        });

        //使用zkClient创建节点，删除节点，验证监听器是否运行
        zkClient.createPersistent("/lg-client");
        Thread.sleep(1000); //只是为了方便观察结果数据
        zkClient.createPersistent("/lg-client/c1");
        Thread.sleep(1000);
        zkClient.delete("/lg-client/c1");
        Thread.sleep(1000);

        zkClient.delete("/lg-client");

        Thread.sleep(Integer.MAX_VALUE);

        /*
        1 监听器可以对不存在的目录进行监听
        2 监听目录下子节点发生改变，可以接收到通知，携带数据有子节点列表
        3 监听目录创建和删除本身也会被监听到
         */
    }
}
