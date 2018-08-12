package com.wz.study.rpc.framework.loadbalance.impl;

import java.util.List;

public class RoundRobinBalance extends AbstractLoadBalance {
    public static int count = 0;

    @Override
    protected String doSelectHost(List<String> hostList) {
        return hostList.get(count++ % hostList.size());
    }
}
