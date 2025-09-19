package com.crane.answer.model.dto.question;

import lombok.Data;

/**
 *
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description   题目答案封装类（用于 AI 评分）
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
