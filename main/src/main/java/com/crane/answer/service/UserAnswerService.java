package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.userAnswer.UserAnswerAddRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerQueryRequest;
import com.crane.answer.model.po.UserAnswer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.UserAnswerResp;

/**
* @author crane
* @description 针对表【user_answer(用户答题记录表（主表）)】的数据库操作Service
* @createDate 2025-09-10 18:40:23
*/
public interface UserAnswerService extends IService<UserAnswer> {

    Long addUserAnswer(UserAnswerAddRequest request);

    UserAnswerResp getUserAnswerRespById(Long id);

    Page<UserAnswerResp> getUserAnswerRespPage(UserAnswerQueryRequest request);

    LambdaQueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest);
}
