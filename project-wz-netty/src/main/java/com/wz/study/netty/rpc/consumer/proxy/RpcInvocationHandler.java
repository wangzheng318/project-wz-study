package com.wz.study.netty.rpc.consumer.proxy;

import com.wz.study.netty.rpc.core.msg.InvokerMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RpcInvocationHandler implements InvocationHandler {

    public RpcInvocationHandler(Class<?> tClass) {
        this.tClass = tClass;
    }

    private Class<?> tClass;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //object方法直接return
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            //传进来的是一个接口，则进行远程调用
            InvokerMsg invokerMsg = new InvokerMsg(tClass.getName(), method.getName(), method.getParameterTypes(), args);
             return rpcInvoker(invokerMsg);
        }
    }

    private Object rpcInvoker(InvokerMsg invokerMsg) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b  = new Bootstrap();
        final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //拆包，粘包处理(编解码器，不写也可以，建议写)
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                        pipeline.addLast(new LengthFieldPrepender(4));

                        //序列化
                        pipeline.addLast("encoder",new ObjectEncoder());
                        pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                        pipeline.addLast(rpcProxyHandler);
                    }
                });

        ChannelFuture future = b.connect("localhost", 8080).sync();
        future.channel().writeAndFlush(invokerMsg).sync();
        future.channel().closeFuture().sync();
        return rpcProxyHandler.getResult();
    }
}
