package com.crane.answer.manager.auth;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author crane
 * @date 2025.09.11 下午7:04
 * @description
 **/
@Configuration
@Slf4j
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**");
//        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
//        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
//                .addPathPatterns("/v3/**")
//                .excludePathPatterns("/user/login",
//                        "/user/register"
//                );


    }
}

