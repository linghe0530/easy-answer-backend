package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultAddRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultEditRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultQueryRequest;
import com.crane.answer.model.vo.ScoringResultResp;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
import com.crane.answer.service.ScoringResultService;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/scoring/result")
@SaCheckLogin
public class ScoringResultController {

    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private AppService appService;
    @Resource
    private ScoringResultService scoringResultService;

    @PostMapping("/add")
    public R<String> addScoringResult(@RequestBody @Valid ScoringResultAddRequest request) {

        Long id = scoringResultService.addScoringResult(request);
        return R.ok(id.toString());
    }

    @PostMapping("/edit")
    public R<Boolean> editScoringResult(@RequestBody @Valid ScoringResultEditRequest request) {
        scoringResultService.editScoringResult(request);
        return R.ok(true);
    }

    @PostMapping("/delete")
    public R<Boolean> deleteScoringResult(@RequestBody @Valid DeleteRequest deleteRequest) {
        scoringResultService.deleteScoringResult(deleteRequest.getId());
        return R.ok(true);
    }

    @GetMapping("/get/resp")
    public R<ScoringResultResp> getScoringResultRespById(Long id) {
        return R.ok();
    }

    @PostMapping("/list/page/resp")
    public R<Page<ScoringResultResp>> listScoringResultRespPage(@RequestBody ScoringResultQueryRequest request) {
        int size = request.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ScoringResultResp> page = scoringResultService.listScoringResultRespPage(request);
        // 获取封装类
        return R.ok(page);
    }

    @PostMapping("/my/list/page/resp")
    public R<Page<ScoringResultResp>> listMyScoringResultRespPage(@RequestBody ScoringResultQueryRequest request) {
        return R.ok();
    }


}
