package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.ai.AiManager;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.chart.ChartAddRequest;
import com.crane.answer.model.dto.chart.ChartEditRequest;
import com.crane.answer.model.dto.chart.ChartQueryRequest;
import com.crane.answer.model.dto.chart.GenChartByAIRequest;
import com.crane.answer.model.po.Chart;
import com.crane.answer.model.vo.BiResponse;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.ChartService;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author crane
 * @date 2025.09.15 下午2:14
 * @description
 **/
@RestController
@RequestMapping("/chart")
@SaCheckLogin
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;
    @Resource
    private ThreadPoolExecutor chartThreadPoolExecutor;

    @PostMapping("/add")
    public R<Long> addChart(@RequestBody ChartAddRequest chartAddRequest) {
        var chatId = chartService.addChart(chartAddRequest);
        return R.ok(chatId);
    }

    @PostMapping("/delete")
    public R<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest) {
        chartService.deleteChart(deleteRequest);
        return R.ok(true);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public R<Chart> getChartById(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return R.ok(chart);
    }

    @PostMapping("/list/page")
    public R<Page<Chart>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long size = chartQueryRequest.getPageSize();
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Chart> chartPage = chartService.selectChartByPage(chartQueryRequest);
        return R.ok(chartPage);
    }


    @PostMapping("/my/list/page")
    public R<Page<Chart>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest) {
        long size = chartQueryRequest.getPageSize();
        if (size > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserResp loginUser = userService.getLoginInfo();
        chartQueryRequest.setUserId(loginUser.getId());
        Page<Chart> chartPage = chartService.selectChartByPage(chartQueryRequest);
        return R.ok(chartPage);
    }


    @PostMapping("/edit")
    public R<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest) {
//        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        Chart chart = new Chart();
//        BeanUtils.copyProperties(chartEditRequest, chart);
//        User loginUser = userService.getLoginUser(request);
//        long id = chartEditRequest.getId();
//        // 判断是否存在
//        Chart oldChart = chartService.getById(id);
//        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可编辑
//        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean result = chartService.updateById(chart);
        return R.ok(true);
    }


    @PostMapping("/gen")
    public R<BiResponse> genChartByAI(@ParameterObject @Valid GenChartByAIRequest request, MultipartFile file) {
        BiResponse result = chartService.getChartByAI(file, request);
        return R.ok(result);
    }

    @PostMapping("/gen/async")
    public R<BiResponse> genChartByAIAsync(@ParameterObject @Valid GenChartByAIRequest request, MultipartFile file) {
        BiResponse result = chartService.getChartByAIAsync(file, request);
        return R.ok(result);
    }

    @PostMapping("/gen/mq")
    public R<BiResponse> genChartByAIMq(@ParameterObject @Valid GenChartByAIRequest request, MultipartFile file) {
        BiResponse result = chartService.getChartByAIMq(file, request);
        return R.ok(result);
    }

    @PostMapping("/test")
    public R<String> testExecutor() {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, chartThreadPoolExecutor);
        return R.ok("result");
    }

    @GetMapping("/test2")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        int size = chartThreadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        long taskCount = chartThreadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        long completedTaskCount = chartThreadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        int activeCount = chartThreadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数", activeCount);
        return JSONUtil.toJsonStr(map);
    }
}
