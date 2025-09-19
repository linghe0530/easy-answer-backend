package com.crane.answer.constants;

/**
 * @author crane
 * @date 2025.09.02 上午12:25
 * @description
 **/
public interface RedisConstants {


    String USER_SIGN_IN_REDIS_KEY = "user:signIn";


    /**
     * 获取用户签到key
     *
     * @param year
     * @param userId
     * @return
     */
    static String getUserSignInRedisKey(int year, long userId) {
        return String.format("%s:%s:%s", USER_SIGN_IN_REDIS_KEY, year, userId);
    }
}
