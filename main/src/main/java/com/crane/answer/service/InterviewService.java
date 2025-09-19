package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.dto.question.QuestionQueryRequest;
import com.crane.answer.model.po.Interview;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.InterviewResp;

/**
* @author crane
* @description 针对表【interview_question(题目)】的数据库操作Service
* @createDate 2025-09-18 09:45:28
*/
public interface InterviewService extends IService<Interview> {

    LambdaQueryWrapper<Interview> getQueryWrapper(InterviewQueryRequest request);


    Page<InterviewResp> getInterviewRespPage(InterviewQueryRequest request);
}
