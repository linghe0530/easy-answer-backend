package com.crane.answer.model.dto.question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 生成题目请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;

    /**
     * 题目数
     */
    @Min(value = 1, message = "题目数量不能少于1")
    @Max(value = 20, message = "题目数量不能超过20")
    Integer questionNum = 10;

    /**
     *
     */
    @Min(value = 2, message = "选项数量不能少于2")
    @Max(value = 5, message = "选项数量不能超过5")
    Integer optionNum = 2;

    @Serial
    private static final long serialVersionUID = 1L;
}
