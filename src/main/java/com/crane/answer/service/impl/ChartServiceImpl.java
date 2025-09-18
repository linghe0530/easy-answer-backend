package com.crane.answer.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.constants.AiConstants;
import com.crane.answer.constants.FileConstants;
import com.crane.answer.constants.MqConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.ai.AiManager;
import com.crane.answer.manager.limiter.RedisLimiterManager;
import com.crane.answer.manager.mq.MessageManger;
import com.crane.answer.mapper.ChartMapper;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.chart.ChartAddRequest;
import com.crane.answer.model.dto.chart.ChartQueryRequest;
import com.crane.answer.model.dto.chart.GenChartByAIRequest;
import com.crane.answer.model.enums.ChartStatusEnum;
import com.crane.answer.model.po.Chart;
import com.crane.answer.model.vo.BiResponse;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.ChartService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.ExcelUtils;
import com.crane.answer.utils.FileUtils;
import com.crane.answer.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author crane
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2025-09-15 14:18:39
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {
    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;
    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Resource
    private ThreadPoolExecutor chartThreadPoolExecutor;
    @Resource
    private MessageManger messageManger;

    @Override
    public Long addChart(ChartAddRequest chartAddRequest) {
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        UserResp loginUser = userService.getLoginInfo();
        chart.setUserId(loginUser.getId());
        boolean result = this.save(chart);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return chart.getId();
    }

    @Override
    public void deleteChart(DeleteRequest deleteRequest) {
        UserResp loginUser = userService.getLoginInfo();
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldChart = this.getById(id);
        if (oldChart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean isSuccess = this.removeById(id);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public BiResponse getChartByAI(MultipartFile file, GenChartByAIRequest request) {
        UserResp loginInfo = userService.getLoginInfo();
        validateFile(file, loginInfo);
        StringBuilder userInput = new StringBuilder();
        String name = request.getName();
        String chartType = request.getChartType();
        String goal = request.getGoal();
        String userGoal = goal;
        //构造用户prompt
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append("分析需求:").append(userGoal).append("\n");
        String csvResult = ExcelUtils.excelToCsv(file);
        userInput.append("原始数据:").append(csvResult).append("\n");
        log.info("用户prompt:{}", userInput);
        String result = aiManager.doRequest(AiConstants.prompt, userInput.toString());
        String[] split = result.split("【【【【【");
        if (split.length < 3) {
            handleChartUpdateError(null, "AI 生成错误");
        }
        String genChart = split[1].trim();
        String genResult = split[2].trim();
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(name);
        chart.setChartData(csvResult);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setStatus(ChartStatusEnum.SUCCEED.getCode());
        chart.setUserId(loginInfo.getId());
        boolean success = this.save(chart);
        if (!success) {
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        BiResponse response = new BiResponse();
        response.setChartId(chart.getId());
        response.setGenChart(genChart);
        response.setGenResult(genResult);
        return response;


    }

    public Chart saveChart(MultipartFile file, GenChartByAIRequest request, UserResp loginInfo) {
        String csvResult = ExcelUtils.excelToCsv(file);
        String name = request.getName();
        String chartType = request.getChartType();
        String goal = request.getGoal();
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(name);
        chart.setChartData(csvResult);
        chart.setChartType(chartType);
        chart.setStatus(ChartStatusEnum.WAIT.getCode());
        chart.setUserId(loginInfo.getId());
        boolean success = this.save(chart);
        if (!success) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return chart;
    }

    @Override
    public BiResponse getChartByAIAsync(MultipartFile file, GenChartByAIRequest request) {
        UserResp loginInfo = userService.getLoginInfo();
        validateFile(file, loginInfo);
        Chart chart = saveChart(file, request, loginInfo);
        StringBuilder userInput = new StringBuilder();
        String userGoal = chart.getGoal();
        //构造用户prompt
        if (StringUtils.isNotBlank(chart.getChartType())) {
            userGoal += "，请使用" + chart.getChartType();
        }
        userInput.append("分析需求:").append(userGoal).append("\n");
        userInput.append("原始数据:").append(chart.getChartData()).append("\n");
        CompletableFuture.runAsync(() -> {
            Chart updateChart = new Chart();
            updateChart.setStatus(ChartStatusEnum.RUNNING.getCode());
            updateChart.setId(chart.getId());
            boolean updateSuccess = this.updateById(updateChart);
            if (!updateSuccess) {
                handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
                return;
            }
            String result = aiManager.doRequest(AiConstants.prompt, userInput.toString());
            Long chartId = chart.getId();
            updateChartResult(result, chartId);
        }, chartThreadPoolExecutor).exceptionally(ex -> {
            // 打印完整异常堆栈信息
            log.error("异步处理图表任务失败：", ex);
            // 发生异常时更新图表状态为失败
            return null;
        });


        BiResponse response = new BiResponse();
        response.setChartId(chart.getId());
        return response;
    }

    @Override
    public void updateChartResult(String result, Long chartId) {
        String[] split = result.split("【【【【【");

        if (split.length < 3) {
            handleChartUpdateError(chartId, "AI 生成错误");
            return;
        }
        String genChart = split[1].trim();
        String genResult = split[2].trim();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        try {
            JsonUtils.parse(genChart, Map.class);
        } catch (Exception e) {
            handleChartUpdateError(chartId, "AI生成图表错误,请重新生成");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI生成图表错误,请重新生成");
        }

        updateChartResult.setStatus(ChartStatusEnum.SUCCEED.getCode());
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult) {
            handleChartUpdateError(chartId, "更新图表成功状态失败");
        }
    }

    @Override
    public BiResponse getChartByAIMq(MultipartFile file, GenChartByAIRequest request) {
        UserResp loginInfo = userService.getLoginInfo();
        //校验文件
        validateFile(file, loginInfo);
        //构造图表信息
        Chart chart = saveChart(file, request, loginInfo);
        //发送消息开始生成
        messageManger.sendMessage(MqConstants.CHART_EXCHANGE, MqConstants.CHART_ROUTING_KEY, String.valueOf(chart.getId()));
        BiResponse response = new BiResponse();
        response.setChartId(chart.getId());
        return response;
    }


    @Override
    public String buildUserInput(Chart chart) {
        StringBuilder userInput = new StringBuilder();
        String chartType = chart.getChartType();
        String userGoal = chart.getGoal();
        String chartData = chart.getChartData();
        //构造用户prompt
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append("分析需求:").append(userGoal).append("\n");
        userInput.append("原始数据:").append(chartData).append("\n");
        return userInput.toString();
    }


    private void validateFile(MultipartFile file, UserResp loginInfo) {
        redisLimiterManager.doRateLimit("genChatAI_" + loginInfo.getId());
        long fileSize = file.getSize();
        String originalFilename = file.getOriginalFilename();
        if (fileSize > FileConstants.ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过1M");
        }
        // 校验文件后缀
        String suffix = FileUtils.getSuffix(originalFilename);
        if (!FileConstants.EXCEL_FILE_SUFFIX_LIST.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不正确");
        }
    }

    @Override
    public void handleChartUpdateError(Long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(ChartStatusEnum.FAILED.getCode());
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败{},{}", chartId, execMessage);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, execMessage);
        }
    }

    @Override
    public Page<Chart> selectChartByPage(ChartQueryRequest chartQueryRequest) {
        int current = chartQueryRequest.getCurrent();
        int pageSize = chartQueryRequest.getPageSize();

        return this.page(new Page<>(current, pageSize),
                this.getQueryWrapper(chartQueryRequest));
    }

    @Override
    public LambdaQueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {

        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();

        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String name = chartQueryRequest.getName();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        Long userId = chartQueryRequest.getUserId();

        queryWrapper.eq(id != null, Chart::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(name), Chart::getName, name);
        queryWrapper.eq(StringUtils.isNotBlank(goal), Chart::getGoal, goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), Chart::getChartType, chartType);
        queryWrapper.eq(userId != null, Chart::getUserId, userId);
        queryWrapper.orderByDesc(Chart::getCreateTime);
        return queryWrapper;
    }


}




