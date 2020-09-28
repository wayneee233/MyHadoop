package com.lagou.zk.demo;


import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

//使用监听器监听节点数据的变化
public class Get_Data_Change {

    public static void main(String[] args) throws InterruptedException {
        // 获取zkClient对象
        final ZkClient zkClient = new ZkClient("linux121:2181");
        //设置自定义的序列化类型,否则会报错！！
        zkClient.setZkSerializer(new ZkStrSerializer());


        //判断节点是否存在，不存在创建节点并赋值
        final boolean exists = zkClient.exists("/lg-client1");
        if (!exists) {
            zkClient.createEphemeral("/lg-client1", "123");
        }

        //注册监听器，节点数据改变的类型，接收通知后的处理逻辑定义
        zkClient.subscribeDataChanges("/lg-client1", new IZkDataListener() {
            public void handleDataChange(String path, Object data) throws Exception {
                //定义接收通知之后的处理逻辑
                System.out.println(path + " data is changed ,new data " + data);
            }

            //数据删除--》节点删除
            public void handleDataDeleted(String path) throws Exception {
                System.out.println(path + " is deleted!!");
            }
        });

        //更新节点的数据，删除节点，验证监听器是否正常运行
        final Object o = zkClient.readData("/lg-client1");
        System.out.println(o);

        zkClient.writeData("/lg-client1", "new data");
        Thread.sleep(1000);

        //删除节点
        zkClient.delete("/lg-client1");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
