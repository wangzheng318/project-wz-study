package com.wz.study.rpc.client;

import com.wz.study.rpc.framework.importer.Importer;
import com.wz.study.rpc.framework.spring.ApplicationContext;

import java.util.concurrent.CountDownLatch;

public class ClientContainer {
    public static void main(String[] args) {
//        Importer importer = new Importer();
//        ApplicationContext applicationContext = importer.importer("com.wz.study.rpc");
//        DemoController demoController = applicationContext.getBean(DemoController.class);
//        System.out.println("操作结果："+demoController.echo("Spring di"));

        testMulitiClient();

    }

    public static void testMulitiClient(){
        int threadCount = 200;
        Importer importer = new Importer();
        ApplicationContext applicationContext = importer.importer("com.wz.study.rpc");
        DemoController demoController = applicationContext.getBean(DemoController.class);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i=0;i<threadCount;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                    System.out.println(demoController.echo("world_"+Thread.currentThread().getName()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Thread_"+i).start();
            countDownLatch.countDown();
        }
    }
}
