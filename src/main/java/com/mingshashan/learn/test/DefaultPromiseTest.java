package com.mingshashan.learn.test;

import io.netty.util.concurrent.*;

public class DefaultPromiseTest {

    public static void main(String[] args) {
        // 构造线程池
        EventExecutor executor = new DefaultEventExecutor();

        // 创建DefaultPromise实例
        Promise promise = new DefaultPromise(executor);

        promise.addListeners((GenericFutureListener<Future<Integer>>) future -> {
            if (future.isSuccess()) {
                System.out.println("任务结束，结果： " + future.get());
            } else {
                System.out.println("任务失败，异常： " + future.cause());
            }
        }, (GenericFutureListener<Future<Integer>>) future -> {
            if (future.isSuccess()) {
                System.out.println("执行完成，结果： " + future.get());
            } else {
                System.out.println("执行失败，结果： " + future.get());
            }
        });

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                promise.setSuccess(666666);
            }
        });

        try {
            promise.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownGracefully();
        }
    }
}
