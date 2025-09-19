package com.crane.answer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.vo.InterviewResp;
import com.crane.answer.service.InterviewService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.18 上午10:02
 * @description
 **/
@RestController
@RequestMapping("/interview")
public class InterviewController {
    @Resource
    private InterviewService interviewService;

    @GetMapping("/get/resp")
    public R<InterviewResp> getInterviewRespById(Long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
//        // 检测和处置爬虫（可以自行扩展为 - 登录后才能获取到答案）
//        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            crawlerDetect(loginUser.getId());
//        }
//        // 友情提示，对于敏感的内容，可以再打印一些日志，记录用户访问的内容
//        // 查询数据库
//        Question question = interviewService.getById(id);
//        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
//        // 获取封装类
//        return R.ok(interviewService.getInterviewResp(question, request));
        return R.ok();
    }

    @PostMapping("/page/resp")
    public R<Page<InterviewResp>> listInterviewRespByPage(@RequestBody InterviewQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        long size = request.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 获取封装类
        return R.ok(interviewService.getInterviewRespPage(request));
    }

    @PostMapping("/my/page/resp")
    public R<Page<InterviewResp>> listMyInterviewRespByPage(@RequestBody InterviewQueryRequest request) {
//        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
//        // 补充查询条件，只查询当前登录用户的数据
//        User loginUser = userService.getLoginUser(request);
//        questionQueryRequest.setUserId(loginUser.getId());
//        long current = questionQueryRequest.getCurrent();
//        long size = questionQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        Page<Interview> questionPage = interviewService.page(new Page<>(current, size),
//                interviewService.getQueryWrapper(questionQueryRequest));
//        // 获取封装类
//        return R.ok(interviewService.getInterviewRespPage(questionPage, request));
        return R.ok();
    }

}
