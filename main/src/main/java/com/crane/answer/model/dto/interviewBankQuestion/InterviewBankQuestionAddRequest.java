package com.crane.answer.model.dto.interviewBankQuestion;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建题库题目关联请求
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewBankQuestionAddRequest implements Serializable {





    /**
     * 题库 id
     */
    private Long interviewBankId;
    /**
     * 题目 id
     */
    private Long interviewId;

    @Serial
    private static final long serialVersionUID = 1L;
}