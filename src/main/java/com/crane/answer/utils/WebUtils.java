package com.crane.answer.utils;

import com.crane.answer.common.R;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author crane
 * @date 2025.06.28 下午11:50
 * @description
 **/
public class WebUtils {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ThrowUtils.throwIf(attributes == null, ErrorCode.SYS_ERROR, "请求无效");
        return attributes.getRequest();
    }

    public static void renderError(HttpServletRequest request, HttpServletResponse response, int code, String msg) {


        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtils.toJson(R.error(code, msg)));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYS_ERROR);
        }

    }

    public static <T> void renderSuccess(HttpServletRequest request, HttpServletResponse response, T data) {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtils.toJson(R.ok(data)));
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYS_ERROR);
        }

    }
}
