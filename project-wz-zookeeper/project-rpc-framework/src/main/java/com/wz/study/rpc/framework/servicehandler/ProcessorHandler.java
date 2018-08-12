package com.wz.study.rpc.framework.servicehandler;

import com.wz.study.rpc.framework.transportation.dto.RpcRequest;
import com.wz.study.rpc.framework.transportation.dto.RpcResponse;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ProcessorHandler implements Runnable {
    private Socket socket;
    private Map<String, Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(in);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = this.handlerRequest(rpcRequest);
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Object handlerRequest(RpcRequest rpcRequest) {
        Object result = null;
        try {
            Object service = handlerMap.get(rpcRequest.getServiceName());
            String className = service.getClass().getName();
            String methodName = rpcRequest.getMethodName();
            Class[] parameterType = rpcRequest.getParameterTypes();
            Object[] args = rpcRequest.getArgs();

            Class c = Class.forName(className);
            Method method = c.getDeclaredMethod(methodName, parameterType);
            method.setAccessible(true);
            result = method.invoke(service, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
