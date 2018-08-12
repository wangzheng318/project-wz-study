package com.wz.study.rpc.server.impl;

import com.wz.study.rpc.framework.annotation.Contract;
import com.wz.study.rpc.framework.annotation.Implement;
import com.wz.study.rpc.service.ServiceDemo;

/**
 * 业务处理demo
 */
@Implement(contract = ServiceDemo.class,version = "1.0.0")
public class ServiceDemoImpl implements ServiceDemo {
    @Override
    public String echo(String message) {
        System.out.printf("receive message:%s \r\n",message);
        String result = "Hello ,"+message;
        System.out.printf("response:%s\r\n",result);
        return result;
    }
}
