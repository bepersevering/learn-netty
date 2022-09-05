package com.mingshashan.learn.demo.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class LearnHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast("codec", new HttpServerCodec())
                .addLast("compressor", new HttpContentCompressor())
                .addLast("aggregator", new HttpObjectAggregator(Character.MAX_VALUE))
                .addLast("handler", new LearnHttpCustomerHandler());
    }
}
