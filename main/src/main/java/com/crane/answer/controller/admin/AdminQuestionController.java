package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.app.AppQueryRequest;
import com.crane.answer.model.dto.question.QuestionQueryRequest;
import com.crane.answer.model.dto.question.QuestionUpdateRequest;
import com.crane.answer.model.po.Question;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
import com.crane.answer.service.QuestionService;
import com.crane.answer.service.UserAnswerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/v3/question")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminQuestionController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private QuestionService questionService;

    @PostMapping("/list/page")
    public R<Page<Question>> listQuestionPage(@RequestBody QuestionQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 查询数据库
        Page<Question> pageResp = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(request));
        return R.ok(pageResp);
    }

    @PostMapping("/update")
    public R<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest request) {
        return R.ok();
    }
}
