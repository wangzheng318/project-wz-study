package com.wz.study.netty.rpc.provider;

import com.wz.study.netty.rpc.api.EchoService;

public class EchoServerImpl implements EchoService {
    @Override
    public String echo(String message) {
        System.out.printf("receive:{%s}\r\n",message);
        String result = "Hello,"+message;
        System.out.printf("response:{%s}\r\n",result);
        return result;
    }
}
