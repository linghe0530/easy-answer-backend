package com.crane.answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.dto.interviewBankQuestion.InterviewBankQuestionQueryRequest;
import com.crane.answer.model.po.InterviewBankQuestion;
import com.crane.answer.service.InterviewBankQuestionService;
import com.crane.answer.mapper.InterviewBankQuestionMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * @author crane
 * @description 针对表【interview_bank_question(题库题目)】的数据库操作Service实现
 * @createDate 2025-09-18 09:45:28
 */
@Service
public class InterviewBankQuestionServiceImpl extends ServiceImpl<InterviewBankQuestionMapper, InterviewBankQuestion>
        implements InterviewBankQuestionService {

    @Override
    public Wrapper<InterviewBankQuestion> getQueryWrapper(InterviewBankQuestionQueryRequest questionBankQuestionQueryRequest) {
        LambdaQueryWrapper<InterviewBankQuestion> queryWrapper = new LambdaQueryWrapper<>();
        if (questionBankQuestionQueryRequest == null) {
            return queryWrapper;
        }

        // 1. 从请求对象中提取参数（与原逻辑一致）
        Long id = questionBankQuestionQueryRequest.getId();
        Long notId = questionBankQuestionQueryRequest.getNotId();
        Long questionBankId = questionBankQuestionQueryRequest.getInterviewBankId();
        Long questionId = questionBankQuestionQueryRequest.getInterviewId();
        Long userId = questionBankQuestionQueryRequest.getUserId();

        // 2. 精确查询条件（替换硬编码字段名为 Lambda 方法引用）
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), InterviewBankQuestion::getId, notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), InterviewBankQuestion::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), InterviewBankQuestion::getUserId, userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), InterviewBankQuestion::getInterviewBankId, questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), InterviewBankQuestion::getInterviewId, questionId);
        return queryWrapper;
    }
}




