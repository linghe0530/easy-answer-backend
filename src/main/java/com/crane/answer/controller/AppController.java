package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.app.AppAddRequest;
import com.crane.answer.model.dto.app.AppEditRequest;
import com.crane.answer.model.dto.app.AppQueryRequest;
import com.crane.answer.model.enums.ReviewStatusEnum;
import com.crane.answer.model.vo.AppResp;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
import com.crane.answer.service.UserAnswerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/app")

public class AppController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private AppService appService;

    @PostMapping("/add")
    @SaCheckLogin
    public R<String> addApp(@RequestBody @Valid AppAddRequest request) {
        Long appId = appService.addApp(request);
        return R.ok(appId.toString());
    }

    @PostMapping("/edit")
    @SaCheckLogin
    public R<Boolean> editApp(@RequestBody @Valid AppEditRequest appEditRequest) {
        appService.editApp(appEditRequest);
        return R.ok(true);
    }

    @PostMapping("/page/resp")
    public R<Page<AppResp>> listAppRespPage(@RequestBody AppQueryRequest request) {
        request.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        Page<AppResp> page = appService.getAppRespPage(request);
        return R.ok(page);
    }

    @GetMapping("/resp")
    @SaCheckLogin
    public R<AppResp> getAppRespById(Long id) {
        AppResp resp = appService.getAppRespById(id);
        return R.ok(resp);
    }

    @PostMapping("/delete")
    @SaCheckLogin
    public R<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest) {
        appService.deleteApp(deleteRequest);
        return R.ok(true);
    }

}
