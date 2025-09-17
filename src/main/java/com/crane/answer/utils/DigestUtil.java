package com.crane.answer.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author crane
 * @date 2025.09.11 下午6:42
 * @description
 **/
public class DigestUtil {

    public static String getEncryptPassword(String password) {
        return DigestUtils.md5Hex(password + "crane");
    }
}
