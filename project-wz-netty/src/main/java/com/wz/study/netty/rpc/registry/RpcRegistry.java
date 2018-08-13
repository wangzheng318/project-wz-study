package com.wz.study.netty.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.rmi.registry.RegistryHandler;
import java.util.concurrent.ConcurrentHashMap;

public class RpcRegistry {
    private  int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start(){
            EventLoopGroup bossGroup =  new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel socketChannel) throws Exception {
                     ChannelPipeline pipeline = socketChannel.pipeline();

                     //拆包，粘包处理(编解码器，不写也可以，建议写)
                     pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                     pipeline.addLast(new LengthFieldPrepender(4));

                     //序列化
                     pipeline.addLast("encoder",new ObjectEncoder());
                     pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                     //自己的业务处理
                     pipeline.addLast(new RpcRegistryHandler());
                 }
             })
            .option(ChannelOption.SO_BACKLOG,128)
            .childOption(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture f = b.bind(this.port).sync();
            System.out.println("RPC Registry start listen at " + this.port);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}
