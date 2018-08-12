package com.wz.study.rpc.framework.spring;

import com.wz.study.rpc.framework.annotation.Reference;
import com.wz.study.rpc.framework.proxy.RpcProxy;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class AnnotaionConfigApplicationContext extends AbstractApplicationContext {

    public AnnotaionConfigApplicationContext(String scanPackage) {
        this.refresh(scanPackage);
    }
    public AnnotaionConfigApplicationContext() {

    }


    @Override
    protected void doSetProperty() throws IllegalAccessException {
        if (!beanMap.isEmpty()) {
            Set<Map.Entry<Class<?>, Object>> entries = beanMap.entrySet();
            for (Map.Entry<Class<?>, Object> entry : entries) {
                Class clazz = entry.getKey();
                Object object = entry.getValue();

                Field[] declaredFields = clazz.getDeclaredFields();
                if (null != declaredFields && 0 < declaredFields.length) {
                    for (Field field : declaredFields) {
                        if (field.isAnnotationPresent(Reference.class)) {
                            Reference reference = field.getAnnotation(Reference.class);
                            field.setAccessible(true);
                            field.set(object, new RpcProxy().getInstants(reference));
                        }
                    }
                }
            }
        }
    }
}
