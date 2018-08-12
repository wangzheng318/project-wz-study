package com.wz.study.rpc.client;

import com.wz.study.rpc.framework.annotation.Contract;
import com.wz.study.rpc.framework.annotation.Reference;
import com.wz.study.rpc.framework.spring.annotation.Controller;
import com.wz.study.rpc.service.ServiceDemo;

@Controller
public class DemoController {
    @Reference(contract = ServiceDemo.class,version = "1.0.0")
    private ServiceDemo serviceDemo;

    public String echo(String message){
        return serviceDemo.echo(message);
    }
}
