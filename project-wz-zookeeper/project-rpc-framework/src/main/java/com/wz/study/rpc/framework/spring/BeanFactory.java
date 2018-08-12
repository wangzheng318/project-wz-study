package com.wz.study.rpc.framework.spring;

public interface BeanFactory {
    <T> T getBean(Class<T> clazz);

//    <T> T getBean(String beanName);
}
