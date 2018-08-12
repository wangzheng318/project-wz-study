package com.wz.study.rpc.framework.loadbalance.impl;

import com.wz.study.rpc.framework.loadbalance.LoadBalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectHost(List<String> hostList) {
       if(null == hostList || 0 == hostList.size()){
           return null;
       }
       if(hostList.size() == 1){
           return hostList.get(0);
       }else{
           return doSelectHost(hostList);
       }
    }

    protected abstract String doSelectHost(List<String> hostList);
}
