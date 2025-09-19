package com.crane.answer.model.dto.interviewBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移除题目题库关系请求
 */
@Data
public class InterviewBankQuestionRemoveRequest implements Serializable {
    /**
     * 题库 id
     */
    private Long interviewBankId;

    /**
     * 题目 id
     */
    private Long interviewId;



    private static final long serialVersionUID = 1L;
}