package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.crane.answer.model.dto.interviewBankQuestion.InterviewBankQuestionQueryRequest;
import com.crane.answer.model.po.InterviewBankQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author crane
* @description 针对表【interview_bank_question(题库题目)】的数据库操作Service
* @createDate 2025-09-18 09:45:28
*/
public interface InterviewBankQuestionService extends IService<InterviewBankQuestion> {

    Wrapper<InterviewBankQuestion> getQueryWrapper(InterviewBankQuestionQueryRequest request);
}
