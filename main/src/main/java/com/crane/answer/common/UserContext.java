package com.crane.answer.common;


import com.crane.answer.model.po.User;

/**
 * @author crane
 * @date 2024.10.11 下午1:25
 * @description
 **/
public class UserContext {

    private static final ThreadLocal<User> T = new ThreadLocal<>();

    public static void setUser(User user) {
        T.set(user);
    }

    public static User getUser() {
        return T.get();
    }

    public static void removeUser() {
        T.remove();
    }
}
