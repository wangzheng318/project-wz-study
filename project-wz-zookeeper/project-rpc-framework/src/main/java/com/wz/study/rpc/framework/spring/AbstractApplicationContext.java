package com.wz.study.rpc.framework.spring;

import com.wz.study.rpc.framework.spring.annotation.Component;
import com.wz.study.rpc.framework.spring.annotation.Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected List<String> componentBeanNameList = new ArrayList<>();
    protected ConcurrentHashMap<Class<?>, Object> beanMap = new ConcurrentHashMap();

    public void refresh(String scanPackage) {
        try {
            doScan(scanPackage);
            doRegister();
            doSetProperty();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("spring ioc success!");
        }
    }

    protected abstract void doSetProperty() throws IllegalAccessException;

    private void doRegister() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(!componentBeanNameList.isEmpty()){
            for (String beanName:componentBeanNameList){
                Class<?> clazz = Class.forName(beanName);
                beanMap.put(clazz,clazz.newInstance());
            }
        }
    }

    private void doScan(String scanPackage) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(scanPackage.replaceAll("[.]", "/"));
        String packagePath = resource.getFile();
        File file = new File(packagePath);
        if (null != file && file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (File subFile : subfiles) {
                if (subFile.isDirectory()) {
                    String subPackage = scanPackage + "." + subFile.getName();
                    this.doScan(subPackage);
                } else {
                    String className = scanPackage + "." + subFile.getName().replace(".class", "");
                    try {
                        Class c = Class.forName(className);
                        if(c.isAnnotationPresent(Controller.class)){
                            componentBeanNameList.add(className);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T)beanMap.get(clazz);
    }
}
