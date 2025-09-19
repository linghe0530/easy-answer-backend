package com.crane.answer.config.chart;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author crane
 * @date 2025.09.16 上午11:11
 * @description
 **/
@Configuration
public class ChartThreadPoolExecutorConfig {

    /**
     * corePoolSize:系统能同时工作的线程数
     * maximumPoolSize:线程池中允许的最大线程数
     * keepAliveTime:当线程池中的线程数大于corePoolSize时，多余的空闲线程的存活时间
     * unit:keepAliveTime的单位
     * workQueue:任务队列，被提交但尚未被执行的任务
     * threadFactory:线程工厂，用于创建线程
     * handler:拒绝策略，当任务无法被执行时（线程池已满且任务队列已满），采取的策略
     */
    @Bean
    public ThreadPoolExecutor chartThreadPoolExecutor() {
        ThreadFactory factory = new ThreadFactory() {
            final AtomicInteger count = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("chart-thread-" + count.getAndDecrement());
                return thread;
            }
        };
        return new ThreadPoolExecutor(8, 16, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(16), factory);

    }
}
