package com.lagou.zk.dislock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//抢锁
//1. 去zk创建临时序列节点，并获取到序号
//2. 判断自己创建节点序号是否是当前节点最小序号，如果是则获取锁
//执行相关操作，最后要释放锁
//3. 不是最小节点，当前线程需要等待，等待你的前一个序号的节点
//被删除，然后再次判断自己是否是最小节点。。。
public class DisClient {

    public DisClient() {
        //初始化zk的/distrilocl节点,会出现线程安全问题
        synchronized (DisClient.class){
            if (!zkClient.exists("/distrilock")) {
                zkClient.createPersistent("/distrilock");
            }
        }

    }

    //前一个节点
    String beforNodePath;

    String currentNoePath;
    //获取到zkClient
    private ZkClient zkClient = new ZkClient("linux121:2181,linux122:2181");
    //把抢锁过程为量部分，一部分是创建节点，比较序号，另一部分是等待锁

    //完整获取锁方法
    public void getDisLock() {
        //获取到当前线程名称
        final String threadName = Thread.currentThread().getName();
        //首先调用tryGetLock
        if (tryGetLock()) {
            //说明获取到锁
            System.out.println(threadName + ":获取到了锁");
        } else {
            // 没有获取到锁，
            System.out.println(threadName + ":获取锁失败,进入等待状态");
            waitForLock();
            //递归获取锁
            getDisLock();
        }

    }

    CountDownLatch countDownLatch = null;

    //尝试获取锁
    public boolean tryGetLock() {
        //创建临时顺序节点,/distrilock/序号
        if (null == currentNoePath || "".equals(currentNoePath)) {
            currentNoePath = zkClient.createEphemeralSequential("/distrilock/", "lock");
        }
        //获取到/distrilock下所有的子节点
        final List<String> childs = zkClient.getChildren("/distrilock");
        //对节点信息进行排序
        Collections.sort(childs); //默认是升序
        final String minNode = childs.get(0);
        //判断自己创建节点是否与最小序号一致
        if (currentNoePath.equals("/distrilock/" + minNode)) {
            //说明当前线程创建的就是序号最小节点
            return true;
        } else {
            //说明最小节点不是自己创建，要监控自己当前节点序号前一个的节点
            final int i = Collections.binarySearch(childs, currentNoePath.substring("/distrilock/".length()));
            //前一个(lastNodeChild是不包括父节点)
            String lastNodeChild = childs.get(i - 1);
            beforNodePath = "/distrilock/" + lastNodeChild;
        }

        return false;
    }

    //等待之前节点释放锁,如何判断锁被释放，需要唤醒线程继续尝试tryGetLock
    public void waitForLock() {

        //准备一个监听器
        final IZkDataListener iZkDataListener = new IZkDataListener() {

            public void handleDataChange(String s, Object o) throws Exception {

            }

            //删除
            public void handleDataDeleted(String s) throws Exception {
                //提醒当前线程再次获取锁
                countDownLatch.countDown();//把值减1变为0，唤醒之前await线程
            }
        };
        //监控前一个节点
        zkClient.subscribeDataChanges(beforNodePath, iZkDataListener);

        //在监听的通知没来之前，该线程应该是等待状态，先判断一次上一个节点是否还存在
        if (zkClient.exists(beforNodePath)) {
            //开始等待,CountDownLatch:线程同步计数器
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();//阻塞，countDownLatch值变为0
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //解除监听
        zkClient.unsubscribeDataChanges(beforNodePath, iZkDataListener);
    }


    //释放锁
    public void deleteLock() {
        if (zkClient != null) {
            zkClient.delete(currentNoePath);
            zkClient.close();
        }
    }
}
