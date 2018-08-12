package com.wz.study.rpc.framework.discovery.impl;

import com.wz.study.rpc.framework.discovery.ServiceDiscovery;
import com.wz.study.rpc.framework.loadbalance.LoadBalance;
import com.wz.study.rpc.framework.loadbalance.impl.RandomBalance;
import com.wz.study.rpc.framework.registry.constants.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class ServiceDiscoveryImpl implements ServiceDiscovery {

    List<String> hostList = null;
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
    public String discover(String serviceName) {
        String resultServerUrl = null;
        String path = ZKConfig.ZK_REGISTRY_PATH+"/"+serviceName;
        try {
            hostList = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException("获取子节点异常；",e);
        }

        //动态发现服务节点的变化
        registerWatcher(path);

        //负载均衡机制
        LoadBalance loadBalance = new RandomBalance();
        resultServerUrl = loadBalance.selectHost(hostList);
        System.out.printf("负载均衡：hostList={%s},selected={%s}\r\n",hostList,resultServerUrl);
       return resultServerUrl;
    }

    private void registerWatcher(final String path){
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework,path,true);

        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                hostList = curatorFramework.getChildren().forPath(path);
            }
        };

        childrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册pathChildren wather异常",e);
        }
    }
}
