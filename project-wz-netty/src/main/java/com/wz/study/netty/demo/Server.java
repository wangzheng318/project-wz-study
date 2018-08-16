package com.wz.study.netty.demo;

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

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
        System.out.println("server init success:listen on port:"+port);
        listen();
    }

    private void listen() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {

                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {

                                    ChannelPipeline pipeline = ch.pipeline();

                                    //处理的拆包、粘包的解、编码器
                                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                                    pipeline.addLast(new LengthFieldPrepender(4));


                                    //处理序列化的解、编码器（JDK默认的序列化）
                                    pipeline.addLast("encoder",new ObjectEncoder());
                                    pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));


                                    //自己的业务逻辑
                                    pipeline.addLast(new ServerHandler());

                                }

                            })
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);
//            .option(ChannelOption.SO_BACKLOG,128)
//            .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture f = b.bind(this.port).sync();
            System.out.println("RPC Registry start listen at " + this.port);
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Server(8088);
    }

}
