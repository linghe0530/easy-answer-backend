package com.crane.answer.model.dto.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建题目请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目内容（json格式）
     */
    @Valid
    @NotNull
    @Size(min = 1, max = 50, message = "问题内容数量必须在1-50之间")
    private List<QuestionContentDTO> questionContent;

    /**
     * 应用 id
     */
    @NotNull(message = "应用id不能为空")
    private Long appId;

    @Serial
    private static final long serialVersionUID = 1L;
}