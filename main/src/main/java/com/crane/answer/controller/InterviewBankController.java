package com.crane.answer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankQueryRequest;
import com.crane.answer.model.dto.question.QuestionQueryRequest;
import com.crane.answer.model.po.Interview;
import com.crane.answer.model.po.InterviewBank;
import com.crane.answer.model.vo.InterviewBankResp;
import com.crane.answer.model.vo.InterviewResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.InterviewBankService;
import com.crane.answer.service.InterviewService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.18 上午10:02
 * @description
 **/
@RestController
@RequestMapping("/interview/bank")
public class InterviewBankController {
    @Resource
    private InterviewBankService interviewBankService;
    @Resource
    private InterviewService interviewService;
    @Resource
    private UserService userService;

    @GetMapping("/get/resp")
    public R<InterviewBankResp> getInterviewBankRespById(@Valid InterviewBankQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        InterviewBankResp resp = interviewBankService.getInterviewBankRespById(request);
        return R.ok(resp);
    }


    @PostMapping("/list/page/resp")
    public R<Page<InterviewBankResp>> listInterviewBankRespByPage(@RequestBody InterviewBankQueryRequest request) {
        long size = request.getPageSize();
        ThrowUtils.throwIf(size > 200, ErrorCode.PARAMS_ERROR);
        return R.ok(interviewBankService.getInterviewBankRespPage(request));
    }


    @PostMapping("/my/list/page/resp")
    public R<Page<InterviewBankResp>> listMyInterviewBankRespByPage(@RequestBody InterviewBankQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        UserResp loginUser = userService.getLoginInfo();
        request.setUserId(loginUser.getId());
        long size = request.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        return R.ok(interviewBankService.getInterviewBankRespPage(request));
    }
}
