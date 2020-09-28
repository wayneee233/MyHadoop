package com.lagou.zk.dislock;

//zk实现分布式锁
public class DisLockTest {
    public static void main(String[] args) {
        //使用10个线程模拟分布式环境
        for (int i = 0; i < 10; i++) {
            new Thread(new DisLockRunnable()).start();//启动线程
        }
    }

    static class DisLockRunnable implements Runnable {

        public void run() {
            //每个线程具体的任务，每个线程就是抢锁，
            final DisClient client = new DisClient();
            client.getDisLock();

            //模拟获取锁之后的其它动作
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //释放锁
            client.deleteLock();
        }
    }
}
