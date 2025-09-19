package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.ReviewRequest;
import com.crane.answer.model.dto.app.AppQueryRequest;
import com.crane.answer.model.dto.app.AppUpdateRequest;
import com.crane.answer.model.po.App;
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
@RequestMapping("/v3/app")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminAppController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private AppService appService;

    @PostMapping("/review")
    public R<Boolean> doAppReview(@RequestBody ReviewRequest request) {
        appService.doAppReview(request);
        return R.ok(true);
    }

    @PostMapping("/list/page")
    public R<Page<App>> listAppPage(@RequestBody AppQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 查询数据库
        Page<App> pageResp = appService.page(new Page<>(current, size),
                appService.getQueryWrapper(request));
        return R.ok(pageResp);
    }

    @PostMapping("/update")
    public R<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest) {
        return R.ok();
    }
}
