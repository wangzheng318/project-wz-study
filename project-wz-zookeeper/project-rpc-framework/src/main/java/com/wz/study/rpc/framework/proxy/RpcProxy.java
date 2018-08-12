package com.wz.study.rpc.framework.proxy;

import java.lang.reflect.Proxy;

public class RpcProxy {
    public <T> T getInstants(final Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaceClass}, new RpcInvocationHandler(interfaceClass));
    }

}
