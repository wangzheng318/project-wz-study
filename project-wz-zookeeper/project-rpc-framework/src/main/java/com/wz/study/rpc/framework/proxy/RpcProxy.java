package com.wz.study.rpc.framework.proxy;

import com.wz.study.rpc.framework.annotation.Reference;

import java.lang.reflect.Proxy;

public class RpcProxy {
    public <T> T getInstants(Reference reference) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{reference.contract()}, new RpcInvocationHandler(reference));
    }
}
