package com.wz.study.netty.rpc.registry;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import com.wz.study.netty.rpc.core.msg.InvokerMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RpcRegistryHandler extends ChannelInboundHandlerAdapter {

    List<String> beanNameList = new ArrayList<>();
    public static ConcurrentHashMap<String,Object> registryMap;

    public RpcRegistryHandler() {
        try {
            this.doScan("com.wz.study.netty.rpc.provider");
            this.register();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

       Object result = new Object();
        InvokerMsg request = (InvokerMsg)msg;
        if(registryMap.containsKey(request.getClassName())){
            Object object = registryMap.get(request.getClassName());
            Method method = object.getClass().getMethod(request.getMethodName(),request.getParameterTypes());
            method.setAccessible(true);
            result = method.invoke(object);
        }
        ctx.writeAndFlush(request);
        ctx.close();
       // ctx.fireChannelRead(msg);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.fireExceptionCaught(cause);
        cause.printStackTrace();
        ctx.close();
    }

    //ioc容器
    private void doScan(String scanPackage) throws ClassNotFoundException {
        String packagePath = Thread.currentThread().getContextClassLoader().getResource(scanPackage.replaceAll("[.]", "/")).getFile();
        File file = new File(packagePath);
        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for(File subFile:files){
                if(subFile.isDirectory()){
                    String subScanPackage = scanPackage+subFile.getName();
                    this.doScan(subScanPackage);
                }else{
                   String className = scanPackage+(subFile.getName().replace(".class",""));
                    beanNameList.add(className);
// Class c = Class.forName(className);
//                   if(c.isAnnotationPresent())
                }
            }
        }
    }

    public void register() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      if(!beanNameList.isEmpty()){
          for(String beanName:beanNameList){
              Class<?> aClass = Class.forName(beanName);
               registryMap.put(aClass.getInterfaces()[0].getName(), aClass.newInstance());
          }
      }
    }
}
