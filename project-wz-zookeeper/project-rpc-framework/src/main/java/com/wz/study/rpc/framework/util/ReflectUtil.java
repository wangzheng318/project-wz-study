package com.wz.study.rpc.framework.util;

import javax.xml.ws.spi.ServiceDelegate;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {

    /**
     * @param className
     * @param methodName
     * @param parameterTypes
     * @param args
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object invoke(String className, String methodName, Class[] parameterTypes, Object... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object data = null;
        try {
            Class c = Class.forName(className);
            Method method = c.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            data = method.invoke(c.newInstance(), args);
        } catch (Exception e) {
            throw e;
        }
        return data;
    }


    public static Object invoke(String className, String methodName, Object... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object data = null;
        try {
            Class[] parameterTypes = null;
            if (null != args && 0 < args.length) {
                parameterTypes = new Class[args.length];
                for (int i=0;i<args.length;i++){
                    parameterTypes[i] = args[i].getClass();
                }
            }
            data = invoke(className,methodName,parameterTypes,args);
        } catch (Exception e) {
            throw e;
        }
        return data;
    }
}
