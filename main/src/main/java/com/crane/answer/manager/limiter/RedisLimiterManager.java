package com.crane.answer.manager.limiter;

import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.09.16 上午12:30
 * @description
 **/
@Component
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;


    /**
     * @param key 区分不同的限流容器
     */
    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 5, Duration.ofSeconds(1));
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.RATE_LIMIT_ERROR);
        }
    }

    /**
     * @param key 区分不同的限流容器
     */
    public void doRateLimit(String key, int count) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, count, Duration.ofDays(1));
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.RATE_LIMIT_ERROR, "非vip用户每日只允许搜索两次");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                sb.append("1");
            }).start();
            sb.append("2");
        }
        System.out.println(sb.length());
        System.out.println(sb);

        StringBuffer sb2 = new StringBuffer();

            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    sb2.append("1");
                }
            }).start();
            new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    sb2.append("2");
                }
            }).start();

        System.out.println(sb2.toString());
        System.out.println(sb2.length());
        Thread.sleep(5000);
    }
}
