package com.crane.answer.model.dto.interviewBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量从题库移除题目关联请求
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewBankQuestionBatchRemoveRequest implements Serializable {


    /**
     * 题库 id
     */
    private Long interviewBankId;

    /**
     * 题目 id 列表
     */
    private List<Long> interviewIdList;

    private static final long serialVersionUID = 1L;
}