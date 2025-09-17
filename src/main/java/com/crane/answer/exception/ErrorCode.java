package com.crane.answer.exception;

import lombok.Getter;

/**
 * @author crane
 * @date 2025.09.01 下午7:46
 * @description
 **/
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok"),
    NO_RESOURCE(1006, "未找到该资源"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "暂无权限访问"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYS_ERROR(50000, "系统开小差了,请联系管理员"),
    OPERATION_ERROR(50001, "操作失败"),
    CUSTOM_ERROR(50002, "自定义异常信息"),
    RATE_LIMIT_ERROR(50003, "操作频繁,请稍后再试"),
    ;

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
