package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.ReviewRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultQueryRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultUpdateRequest;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.ScoringResult;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
import com.crane.answer.service.ScoringResultService;
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
@RequestMapping("/v3/scoring/result")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminScoringResultController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private ScoringResultService scoringResultService;


    @PostMapping("/list/page")
    public R<Page<ScoringResult>> listScoringResultPage(@RequestBody ScoringResultQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        Page<ScoringResult> page = new Page<>(current, pageSize);

        return R.ok(scoringResultService.page(page, scoringResultService.getQueryWrapper(request)));
    }

    @PostMapping("/update")
    public R<Boolean> updateScoringResult(@RequestBody ScoringResultUpdateRequest request) {
        return R.ok();
    }
}
