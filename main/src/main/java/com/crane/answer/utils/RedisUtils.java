package com.crane.answer.utils;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author crane
 * @date 2025.06.25 下午11:20
 * @description
 **/
@Component
public class RedisUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public String getPicturePage(String key) {

        return stringRedisTemplate.opsForValue().get(key);
    }

    public void savePicturePage(String key, String json, Integer timeout) {
        if (StringUtils.isBlank(json)) {
            json = "";
        }
        stringRedisTemplate.opsForValue().set(key, json, timeout, TimeUnit.SECONDS);
    }
}
