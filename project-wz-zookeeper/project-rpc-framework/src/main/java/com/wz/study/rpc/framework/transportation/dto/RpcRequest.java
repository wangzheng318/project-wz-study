package com.wz.study.rpc.framework.transportation.dto;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1834016017660335123L;
    //服务名称：接口名+版本号（用于查找接口对应的实现类）
    public String serviceName;

    //方法名称
    private String methodName;

    //方法参数
    private Object[] args;
    //方法参数类型
    private Class[] parameterTypes;

    public RpcRequest(String serviceName, String methodName, Class[] parameterTypes ,Object[] args) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.args = args;
        this.parameterTypes = parameterTypes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
