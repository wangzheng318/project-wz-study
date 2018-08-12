package com.wz.study.rpc.framework.loadbalance.impl;

import java.util.List;
import java.util.Random;

public class RandomBalance extends AbstractLoadBalance {
    @Override
    protected String doSelectHost(List<String> hostList) {
        return hostList.get(new Random().nextInt(hostList.size()));
    }
}
