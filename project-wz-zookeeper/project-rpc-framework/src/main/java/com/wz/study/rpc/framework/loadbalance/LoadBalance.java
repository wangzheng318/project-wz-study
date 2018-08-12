package com.wz.study.rpc.framework.loadbalance;

import java.util.List;

public interface LoadBalance {
    String selectHost(List<String> hostList);
}
