package com.wz.study.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.logging.Handler;

public class Client {
    private  String hostname;
    private  int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        System.out.printf("client init success:hostname=%s ,port=%s \r\n",hostname,port);

    }

    public void sendMsg(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ClientHandler());
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY,true);
            ChannelFuture future = bootstrap.connect(this.hostname, this.port).sync();
            future.channel().writeAndFlush("hello,server");
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client("127.0.0.1",8080).sendMsg();
    }
}
