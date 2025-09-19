package com.crane.answer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.interviewBankQuestion.InterviewBankQuestionQueryRequest;
import com.crane.answer.model.vo.InterviewBankQuestionResp;
import com.crane.answer.service.InterviewBankQuestionService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crane
 * @date 2025.09.18 上午10:02
 * @description
 **/
@RestController
@RequestMapping("/bank/question")
public class InterviewBankQuestionController {
    @Resource
    private InterviewBankQuestionService interviewBankQuestionService;
    @Resource
    private UserService userService;

    @PostMapping("/page/resp")
    public R<Page<InterviewBankQuestionResp>> listQuestionBankQuestionVOByPage(@RequestBody InterviewBankQuestionQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        Page<InterviewBankQuestion> questionBankQuestionPage = interviewBankQuestionService.page(new Page<>(current, size),
//                interviewBankQuestionService.getQueryWrapper(request));
//        // 获取封装类
//        return R.ok(interviewBankQuestionService.getQuestionBankQuestionVOPage(questionBankQuestionPage, request));
        return R.ok();
    }

    @PostMapping("/my/page/resp")
    public R<Page<InterviewBankQuestionResp>> listMyQuestionBankQuestionVOByPage(@RequestBody InterviewBankQuestionQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
//        // 补充查询条件，只查询当前登录用户的数据
//        User loginUser = userService.getLoginUser(request);
//        questionBankQuestionQueryRequest.setUserId(loginUser.getId());
//        long current = questionBankQuestionQueryRequest.getCurrent();
//        long size = questionBankQuestionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        Page<QuestionBankQuestion> questionBankQuestionPage = questionBankQuestionService.page(new Page<>(current, size),
//                questionBankQuestionService.getQueryWrapper(questionBankQuestionQueryRequest));
//        // 获取封装类
//        return R.ok(questionBankQuestionService.getQuestionBankQuestionVOPage(questionBankQuestionPage, request));
        return R.ok();
    }
}
