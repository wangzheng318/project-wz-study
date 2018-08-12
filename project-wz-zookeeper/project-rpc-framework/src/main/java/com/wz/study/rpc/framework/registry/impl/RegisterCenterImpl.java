package com.wz.study.rpc.framework.registry.impl;

import com.wz.study.rpc.framework.registry.Register;
import com.wz.study.rpc.framework.registry.constants.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class RegisterCenterImpl implements Register {

    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZKConfig.ZK_CONNECTION_STRING)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serverAddress) {
        //注册相应的服务
        String servicePath = ZKConfig.ZK_REGISTRY_PATH + "/" + serviceName;
        try {
            //判断/registories/product-service是否存在，不存在则创建
            if (null == curatorFramework.checkExists().forPath(servicePath)) {
                curatorFramework.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath, "0".getBytes());
            }
            String addressPath = servicePath + "/" + serverAddress;
            String rsNode = curatorFramework
                    .create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath, "0".getBytes());
            System.out.println("服务注册成功：" + rsNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
