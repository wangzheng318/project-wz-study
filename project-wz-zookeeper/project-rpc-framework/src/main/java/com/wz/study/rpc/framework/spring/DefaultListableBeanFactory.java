package com.wz.study.rpc.framework.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanFactory{

    private List<String> beanName = new ArrayList<>();
    private ConcurrentHashMap<Class,Object> concurrentHashMap = new ConcurrentHashMap<>();


    @Override
    public <T> T getBean(Class<T> clazz) {
        return null;
    }
}
