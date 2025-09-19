package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.scoringResult.ScoringResultQueryRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultUpdateRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerQueryRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerUpdateRequest;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
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
@RequestMapping("/v3/answer")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminUserAnswerController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;


    @PostMapping("/list/page")
    public R<Page<UserAnswer>> listUserAnswerPage(@RequestBody UserAnswerQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        Page<UserAnswer> page = new Page<>(current, pageSize);

        return R.ok(userAnswerService.page(page, userAnswerService.getQueryWrapper(request)));
    }

    @PostMapping("/update")
    public R<Boolean> updateUserAnswer(@RequestBody UserAnswerUpdateRequest request) {
        return R.ok();
    }
}
