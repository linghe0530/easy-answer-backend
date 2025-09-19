package com.crane.answer.exception;


import lombok.Getter;

import java.io.Serial;

/**
 * @author crane
 * @date 2025.09.01 下午7:47
 * @description 自定义业务异常
 **/
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1893320493005361680L;
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}