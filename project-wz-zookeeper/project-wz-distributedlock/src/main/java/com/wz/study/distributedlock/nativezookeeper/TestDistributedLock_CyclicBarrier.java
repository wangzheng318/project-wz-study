package com.wz.study.distributedlock.nativezookeeper;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestDistributedLock_CyclicBarrier {
    public static void main(String[] args) {
        System.out.println("Hello,world");
        testDistributiedLock();
    }

    public static void testDistributiedLock() {
        System.out.println("come testDistributedLock....");
        int threadSize = 10;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadSize);

        for (int i = 0; i < threadSize; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName()+"开始进入await()");
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName()+"离开await()");
                    DistributedLock distributedLock = new DistributedLock();
                    distributedLock.lock();
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+" 休息1秒后释放锁");
                    distributedLock.unlock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "Thead" + i).start();

        }
    }
}
