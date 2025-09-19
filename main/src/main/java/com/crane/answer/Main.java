package com.crane.answer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author crane
 * @date 2025.09.11 上午11:46
 * @description
 **/
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableAsync
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Main.class, args);

    }
}