package com.wz.study.netty.rpc.consumer;

import com.wz.study.netty.rpc.api.EchoService;
import com.wz.study.netty.rpc.consumer.proxy.RpcProxy;

public class RpcConsumer {
    public static void main(String[] args) {
//        EchoService echoService = new EchoServerImpl();
//        System.out.println(echoService.echo("zhangsan"));
        EchoService echoService = new RpcProxy().create(EchoService.class);
        System.out.println(echoService.echo("client"));
    }
}
