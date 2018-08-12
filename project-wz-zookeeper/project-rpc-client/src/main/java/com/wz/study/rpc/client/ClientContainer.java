package com.wz.study.rpc.client;

import com.wz.study.rpc.framework.proxy.RpcProxy;
import com.wz.study.rpc.service.ServiceDemo;

public class ClientContainer {
    public static void main(String[] args) {
        ServiceDemo serviceDemo =  new RpcProxy().getInstants(ServiceDemo.class);
        System.out.println(serviceDemo.echo("world"));
    }
}
