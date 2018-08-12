package com.wz.study.rpc.framework;

import com.wz.study.rpc.framework.annotation.Contract;
import com.wz.study.rpc.framework.registry.Register;
import com.wz.study.rpc.framework.servicehandler.ProcessorHandler;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务发布
 */
public class Exporter {

    private ExecutorService executorService;

    //注册中心(注册中心地址)
    private Register registerCenter;
    //服务发布地址(ip:port)
    private String serviceAddress;


    //rpc服务端实现类信息
    private List<String> rpcClassNameList = new ArrayList<>();
    //从注解中解析出接口与实现类之间的映射关系
    private ConcurrentHashMap<String,Object> handlerMap = new ConcurrentHashMap();

    /**
     * 扫描指定包目录下的文件，扫描需要出rpc标记的类
     * @param scanPackage
     */
    public void scan (String scanPackage){
        URL url = Thread.currentThread().getContextClassLoader().getResource(scanPackage.replaceAll("[.]","/"));
        String filePath = url.getFile();
        File file = new File(filePath);
        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (File subFile:files){
                if(subFile.isDirectory()){
                    String subScanPackage = scanPackage+"."+(subFile.getName().replace(".class",""));
                    this.scan(subScanPackage);
                }else{
                    String className = scanPackage+"."+(subFile.getName().replace(".class",""));
                    try {
                        Class clazz = Class.forName(className);
                        if(clazz.isAnnotationPresent(Contract.class)){
                            rpcClassNameList.add(className);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注册rpc实现类信息,便于后续通过接口找到实现类
     */
    public void register(){
        if(!rpcClassNameList.isEmpty()){
            for(String className:rpcClassNameList){
                try {
                    Class c = Class.forName(className);
                    Contract contract = (Contract)c.getAnnotation(Contract.class);
                    String serviceName = contract.value().getName()+"#"+contract.version();
                    handlerMap.put(serviceName,c.newInstance());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 构造函数初始化注册中心与注册服务信息
     *
     * @param registerCenter
     * @param serviceAddress
     */
    public Exporter(Register registerCenter, String serviceAddress) {
        this.registerCenter = registerCenter;
        this.serviceAddress = serviceAddress;
        //线程池数量：CUP 个数*2 多核处理技术（1个CUP双核） 1核可以同时跑2个线程（超线程技术）
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    }

//    /**
//     * 绑定服务名称和服务对象
//     *
//     * @param services
//     */
//    public void bind(Object... services) {
//        for (Object service : services) {
//            Contract contract = service.getClass().getAnnotation(Contract.class);
//            if (null != contract) {
//                Class contractClass = contract.value();
//                String version = contract.version();
//                String serviceName = contractClass.getName() + "_" + version;
//                //保存服务名称与实现类之间的映射关系
//                handlerMap.put(serviceName, service);
//            }
//        }
//    }

    public void publish(String scanPackage) {
        ServerSocket serverSocket = null;
        try {
            //IOC容器_定位、扫描
            this.scan(scanPackage);
            //IOC容器_注册
            this.register();

            //从服务地址信息中取出服务ip及端口号
            String[] addrs = this.serviceAddress.split(":");
            String ip = addrs[0];
            int port = Integer.valueOf(addrs[1]);

            //将服务注册到注册中心上
            for(String serviceName:handlerMap.keySet()){
                registerCenter.register(serviceName,serviceAddress);
                System.out.printf("注册服务成功：%s->%s",serviceName,serviceAddress);
            }

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip,port));
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessorHandler(socket,handlerMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
