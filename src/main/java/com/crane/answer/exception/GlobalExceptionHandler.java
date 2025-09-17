package com.crane.answer.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.crane.answer.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author crane
 * @date 2025.06.27 下午4:52
 * @description
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public R<?> notLoginException(NotLoginException e) {
        log.error("NotLoginException", e);
        return R.error(ErrorCode.NOT_LOGIN_ERROR.getCode(), ErrorCode.NOT_LOGIN_ERROR.getMessage());
    }

    @ExceptionHandler(NotRoleException.class)
    public R<?> notRoleExceptionHandler(NotRoleException e) {
        log.error("NotRoleException", e);
        return R.error(ErrorCode.NO_AUTH_ERROR.getCode(), ErrorCode.NO_AUTH_ERROR.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    public R<?> notPermissionExceptionHandler(NotPermissionException e) {
        log.error("NotPermissionException", e);
        return R.error(ErrorCode.NO_AUTH_ERROR.getCode(), ErrorCode.NO_AUTH_ERROR.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("参数校验异常:", ex);
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            sb.append(errorMessage).append(",");
        });
        return R.error(ErrorCode.PARAMS_ERROR.getCode(), sb.toString());
    }

    //    @ExceptionHandler(RateLimitException.class)
//    public ResponseEntity<Map<String, Object>> handleRateLimitException(RateLimitException e) {
//        log.warn("触发限流: {}", e.getMessage());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", 429);
//        response.put("message", e.getMessage());
//        response.put("success", false);
//
//        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
//    }
//
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("系统异常:", e);
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleException(RuntimeException e) {
        log.error(ErrorCode.SYS_ERROR.getMessage(), e);
        return R.error(ErrorCode.SYS_ERROR.getCode(), ErrorCode.SYS_ERROR.getMessage());
    }
}
