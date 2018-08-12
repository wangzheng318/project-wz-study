package com.wz.study.distributedlock.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 *
 */
public class CuratorLock {
    public static void main(String[] args) {
        useCuratorLock();
    }

    public static void useCuratorLock() {
        CuratorFramework curatorFramework=CuratorFrameworkFactory.builder().build();
        InterProcessMutex interProcessMutex=new InterProcessMutex(curatorFramework,"/locks");
        try {
            interProcessMutex.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
