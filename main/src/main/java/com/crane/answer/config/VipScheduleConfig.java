package com.crane.answer.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author crane
 * @date 2025.09.14 下午4:04
 * @description
 **/
@Configuration
public class VipScheduleConfig {

    @Bean
    public Scheduler vipScheduler() {

        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r, "vip-thread-" + threadNumber.getAndIncrement());
                // 非守护线程
                thread.setDaemon(false);
                return thread;
            }
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10, threadFactory);
        return Schedulers.fromExecutorService(scheduledExecutorService, "vip-scheduler");
    }
}
