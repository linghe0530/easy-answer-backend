package com.crane.answer.manager.black;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonBloomFilter;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

/**
 * @author crane
 * @date 2025.09.18 下午6:46
 * @description
 **/
@Slf4j
public class BlackFilter {


    private static RedissonClient redissonClient;

    public static boolean isBlackIp(String ip) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("blackIp");

        return false;
    }
}
