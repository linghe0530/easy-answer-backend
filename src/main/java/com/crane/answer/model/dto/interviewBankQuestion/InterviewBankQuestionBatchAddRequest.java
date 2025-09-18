package com.crane.answer.model.dto.interviewBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量创建题库题目关联请求
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewBankQuestionBatchAddRequest implements Serializable {

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id 列表
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}