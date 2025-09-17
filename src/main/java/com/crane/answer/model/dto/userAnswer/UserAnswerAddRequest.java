package com.crane.answer.model.dto.userAnswer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建用户答案请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description
 */
@Data
public class UserAnswerAddRequest implements Serializable {

    /**
     * id（用户答案 id，用于保证提交答案的幂等性）
     */
    @NotNull(message = "无效答案")
    private Long id;

    /**
     * 应用 id
     */
    @NotNull(message = "无效应用id")
    private Long appId;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    @Serial
    private static final long serialVersionUID = 1L;
}