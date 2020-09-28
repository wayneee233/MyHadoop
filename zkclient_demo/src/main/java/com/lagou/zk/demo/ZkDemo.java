package com.lagou.zk.demo;

import org.I0Itec.zkclient.ZkClient;

public class ZkDemo {
    public static void main(String[] args) {
        //先获取到zkclient对象,client与zk集群通信端口是2181
        final ZkClient zkClient = new ZkClient("linux121:2181"); //建立了到zk集群的会话
        System.out.println("zkclient is ready");
        //1 创建节点
        zkClient.createPersistent("/la-client/lg-c1", true); //如果需要级联创建，第二个参数设置为true
        System.out.println("path is created");

        //2删除节点
//        zkClient.delete("/la-client");
        zkClient.deleteRecursive("/la-client");//递归删除可以删除非空节点，先删除子节点然后删除父节点
        System.out.println("delete path is success");
    }
}
