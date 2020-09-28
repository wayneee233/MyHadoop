package com.lagou.zk.onoffline;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

//提供时间查询服务
public class TimeService extends Thread {
    private int port = 0;

    public TimeService(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        //通过socket与client进行交流，启动serversocket监听请求
        try {
            //指定监听的端口
            final ServerSocket serverSocket = new ServerSocket(port);

            //保证服务端一直运行
            while (true) {
                final Socket socket = serverSocket.accept();
                //不关心client发送内容，server只考虑发送一个时间值
                final OutputStream out = socket.getOutputStream();
                out.write(new Date().toString().getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
