package com.wz.study.rpc.framework.proxy;

import com.wz.study.rpc.framework.annotation.Reference;
import com.wz.study.rpc.framework.discovery.ServiceDiscovery;
import com.wz.study.rpc.framework.discovery.impl.ServiceDiscoveryImpl;
import com.wz.study.rpc.framework.transportation.Transport;
import com.wz.study.rpc.framework.transportation.dto.RpcRequest;
import com.wz.study.rpc.framework.transportation.impl.TcpTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcInvocationHandler implements InvocationHandler {

    private Reference reference;
    public RpcInvocationHandler(Reference reference) {
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            String serviceName =  reference.contract().getName()+"#"+reference.version();
            RpcRequest rpcRequest = new RpcRequest(serviceName, method.getName(), method.getParameterTypes(), args);
            ServiceDiscovery discovery = new ServiceDiscoveryImpl();
            String discover = discovery.discover(serviceName);
            String[] addressArray = discover.split(":");
            Transport transport = new TcpTransport(addressArray[0],Integer.parseInt(addressArray[1]));
            return transport.send(rpcRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
