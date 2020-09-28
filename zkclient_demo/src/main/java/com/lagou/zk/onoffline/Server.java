package com.lagou.zk.onoffline;

import org.I0Itec.zkclient.ZkClient;

//服务端主要提供了client需要的一个时间查询服务，服务端向zk建立临时节点
public class Server {

    //获取zkclient
    ZkClient zkClient = null;

    private void connectZk() {
        // 创建zkclient
        zkClient = new ZkClient("linux121:2181,linux122:2181");
        //创建服务端建立临时节点的目录
        if (!zkClient.exists("/servers")) {
            zkClient.createPersistent("/servers");
        }
    }

    //告知zk服务器相关信息
    private void saveServerInfo(String ip, String port) {
        final String sequencePath = zkClient.createEphemeralSequential("/servers/server", ip + ":" + port);
        System.out.println("----->>> ,服务器：" + ip + ":" + port + ",向zk保存信息成功，成功上线可以接受client查询");
    }

    public static void main(String[] args) {
        //准备两个服务端启动上线（多线程模拟，一个线程代表一个服务器）
        final Server server = new Server();
        server.connectZk();
        server.saveServerInfo(args[0], args[1]);
        //提供时间服务的线程没有启动，创建一个线程类，可以接收socket请求
        new TimeService(Integer.parseInt(args[1])).start();
    }
}
