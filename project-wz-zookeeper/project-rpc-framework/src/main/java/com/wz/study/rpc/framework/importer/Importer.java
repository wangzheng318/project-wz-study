package com.wz.study.rpc.framework.importer;

import com.wz.study.rpc.framework.annotation.Implement;
import com.wz.study.rpc.framework.spring.AbstractApplicationContext;
import com.wz.study.rpc.framework.spring.AnnotaionConfigApplicationContext;
import com.wz.study.rpc.framework.spring.ApplicationContext;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Importer {

    public ApplicationContext importer(String scanPackage){
        ApplicationContext applicationContext = new AnnotaionConfigApplicationContext();
        applicationContext.refresh(scanPackage);
        return applicationContext;
    }
}
