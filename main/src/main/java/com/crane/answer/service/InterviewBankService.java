package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.interviewBank.InterviewBankAddRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankQueryRequest;
import com.crane.answer.model.po.InterviewBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.InterviewBankResp;

/**
 * @author crane
 * @description 针对表【interview_question_bank(题库)】的数据库操作Service
 * @createDate 2025-09-18 09:45:28
 */
public interface InterviewBankService extends IService<InterviewBank> {

    Wrapper<InterviewBank> getQueryWrapper(InterviewBankQueryRequest questionBankQueryRequest);

    InterviewBankResp getInterviewBankResp(InterviewBank bank);

    Long addInterviewBank(InterviewBankAddRequest request);

    void deleteInterviewBank(Long id);

    Page<InterviewBankResp> getInterviewBankRespPage(InterviewBankQueryRequest request);

    InterviewBankResp getInterviewBankRespById(InterviewBankQueryRequest request);
}
