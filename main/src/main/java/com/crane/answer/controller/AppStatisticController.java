package com.crane.answer.controller;

import com.crane.answer.common.R;
import com.crane.answer.mapper.UserAnswerMapper;
import com.crane.answer.model.dto.statistic.AppAnswerCountDTO;
import com.crane.answer.model.dto.statistic.AppAnswerResultCountDTO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/app/statistic")
public class AppStatisticController {
    @Resource
    private UserAnswerMapper userAnswerMapper;

    @GetMapping("/answer_count")
    public R<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return R.ok(userAnswerMapper.selectAppAnswerCount());
    }

    @GetMapping("/answer_result_count")
    public R<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(@NotNull(message = "appId不能为空") Long appId) {
        return R.ok(userAnswerMapper.selectAppAnswerResultCount(appId));
    }

}
