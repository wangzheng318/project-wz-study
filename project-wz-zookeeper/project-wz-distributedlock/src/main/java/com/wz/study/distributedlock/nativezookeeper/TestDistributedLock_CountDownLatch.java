package com.wz.study.distributedlock.nativezookeeper;

import java.util.concurrent.CountDownLatch;

public class TestDistributedLock_CountDownLatch {
    public static void main(String[] args) {
        System.out.println("Hello,world");
        testDistributiedLock();
    }

    public static void testDistributiedLock() {
        System.out.println("come testDistributedLock....");
        int threadSize = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    DistributedLock distributedLock = new DistributedLock();
                    distributedLock.lock();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+" 休息1秒后释放锁");
                    distributedLock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Thead" + i).start();

            countDownLatch.countDown();
        }
    }
}
