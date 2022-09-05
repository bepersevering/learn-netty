package com.mingshashan.learn.demo.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class LearnHttpServer {

    public static void main(String[] args) {
        final int port = 11071;
        new LearnHttpServer().start(port);
    }

    public void start(final int port) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(1);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_RCVBUF, 128)
                .option(ChannelOption.SO_SNDBUF, 128)
                .option(ChannelOption.SO_TIMEOUT, 300)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new LearnHttpServerInitializer());

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("Http server started. Listening on " + port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
