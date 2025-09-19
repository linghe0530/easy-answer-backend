package com.crane.answer.scoring.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author crane
 * @date 2025.09.10 下午9:19
 * @description
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScoringStrategyConfig {
    /**
     * 应用类型
     *
     * @return
     */
    int appType();

    /**
     * 评分策略
     *
     * @return
     */
    int scoringStrategy();
}
