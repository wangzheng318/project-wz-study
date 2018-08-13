package com.wz.study.netty.rpc.consumer.proxy;

import java.lang.reflect.Proxy;

public class RpcProxy {

    public <T> T create(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{interfaceClass},new RpcInvocationHandler(interfaceClass));
    }
}
