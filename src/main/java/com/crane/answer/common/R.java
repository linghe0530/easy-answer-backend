package com.crane.answer.common;

import com.crane.answer.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "通用响应结果")
public class R<T> {
    @Schema(description = "业务状态码，20000-成功，其它-失败")
    private int code;
    @Schema(description = "响应消息", example = "OK")
    private String msg;
    @Schema(description = "响应数据")
    private T data;

    public static <T> R<T> ok() {
        return new R<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }


    public static <T> R<T> ok(T data) {
        return new R<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(ErrorCode.SYS_ERROR.getCode(), msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


}
