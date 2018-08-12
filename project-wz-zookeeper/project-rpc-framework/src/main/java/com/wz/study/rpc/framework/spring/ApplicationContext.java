package com.wz.study.rpc.framework.spring;

import com.wz.study.rpc.framework.spring.annotation.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ApplicationContext extends BeanFactory {
    /**
     *  启动spring容器
     * @param scanPackage
     */
    void refresh(String scanPackage);
}
