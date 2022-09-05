package com.mingshashan.learn.demo.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

@ChannelHandler.Sharable
public class LearnHttpCustomerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        if (httpObject instanceof HttpRequest) {
            FullHttpRequest msg = (FullHttpRequest) httpObject;
            String content = String.format("Receive http request, uri: %s, method: %s, content: %s%n", msg.uri(), msg.method(), msg.content().toString(CharsetUtil.UTF_8));

            System.out.println(content);
            // ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(PooledByteBufAllocator.defaultPageSize());

            FullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), OK, getContent());
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            httpHeaders.set(CONTENT_TYPE, TEXT_PLAIN).setInt(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().add(httpHeaders);

            ctx.writeAndFlush(response).addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("服务端响应成功.....");
                }
            });
        }
    }

    public static ByteBuf getContent() {

        String path = "/Users/kaifazhongxin/build/02.learn/01.code/01.java/learn-netty/src/main/resources/data/data.json";

        ByteBuf buf = Unpooled.buffer(1937999);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File(path), "r")) {

            FileChannel fileChannel = randomAccessFile.getChannel();
            buf.writeBytes(fileChannel, 0L, (int) fileChannel.size());
            return buf;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        ByteBuf buf = getContent();
        System.out.println(buf.toString(Charset.defaultCharset()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
