package com.wz.study.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

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
            Bootstrap b = new Bootstrap();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //处理的拆包、粘包的解、编码器
                            pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
                            pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));

                            //处理序列化的解、编码器（JDK默认的序列化）
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //自己的业务逻辑
                            pipeline.addLast("handler",new ClientHandler());
                        }

                    });

            ChannelFuture f = b.connect("localhost",8088).sync();
            f.channel().writeAndFlush("Hello").sync();
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Client("127.0.0.1",8088).sendMsg();
    }
}
