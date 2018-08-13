package com.wz.study.netty.rpc.core.msg;

import java.io.Serializable;

public class InvokerMsg implements Serializable{
    private static final long serialVersionUID = -2976930654163921769L;
    private String className;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] args;

    public InvokerMsg(String className, String methodName, Class[] parameterTypes, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
