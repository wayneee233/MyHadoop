package com.lagou.zk.onoffline;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 注册监听zk指定目录，
//维护自己本地一个servers信息，收到通知要进行更新
//发送时间查询请求并接受服务端返回的数据
public class Client {
    //获取zkclient
    ZkClient zkClient = null;

    //维护一个serversi 信息集合
    ArrayList<String> infos = new ArrayList<String>();

    private void connectZk() {
        // 创建zkclient
        zkClient = new ZkClient("linux121:2181,linux122:2181");
        //第一次获取服务器信息,所有的子节点
        final List<String> childs = zkClient.getChildren("/servers");
        for (String child : childs) {
            //存储着ip+port
            final Object o = zkClient.readData("/servers/" + child);
            infos.add(String.valueOf(o));
        }

        //对servers目录进行监听
        zkClient.subscribeChildChanges("/servers", new IZkChildListener() {
            public void handleChildChange(String s, List<String> children) throws Exception {
                //接收到通知，说明节点发生了变化，client需要更新infos集合中的数据
                ArrayList<String> list = new ArrayList<String>();
                //遍历更新过后的所有节点信息
                for (String path : children) {
                    final Object o = zkClient.readData("/servers/" + path);
                    list.add(String.valueOf(o));
                }

                //最新数据覆盖老数据
                infos = list;
                System.out.println("--》接收到通知，最新服务器信息为：" + infos);
            }
        });
    }


    //发送时间查询的请求
    public void sendRequest() throws IOException {
        //目标服务器地址
        final Random random = new Random();
        final int i = random.nextInt(infos.size());
        final String ipPort = infos.get(i);
        final String[] arr = ipPort.split(":");

        //建立socket连接

        final Socket socket = new Socket(arr[0], Integer.parseInt(arr[1]));
        final OutputStream out = socket.getOutputStream();
        final InputStream in = socket.getInputStream();
        //发送数据
        out.write("query time".getBytes());
        out.flush();
        //接收返回结果
        final byte[] b = new byte[1024];
        in.read(b);//读取服务端返回数据
        System.out.println("client端接收到server:+" + ipPort + "+返回结果：" + new String(b));


        //释放资源
        in.close();
        out.close();
        socket.close();
    }


    public static void main(String[] args) throws InterruptedException {

        final Client client = new Client();
        client.connectZk();   //监听器逻辑
        while (true) {
            try {
                client.sendRequest(); //发送请求
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    client.sendRequest();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            //每隔几秒中发送一次请求到服务端
            Thread.sleep(2000);
        }
    }
}
