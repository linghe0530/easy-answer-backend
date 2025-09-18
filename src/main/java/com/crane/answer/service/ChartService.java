package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.chart.ChartAddRequest;
import com.crane.answer.model.dto.chart.ChartQueryRequest;
import com.crane.answer.model.dto.chart.GenChartByAIRequest;
import com.crane.answer.model.po.Chart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.BiResponse;
import org.springframework.web.multipart.MultipartFile;

/**
* @author crane
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2025-09-15 14:18:39
*/
public interface ChartService extends IService<Chart> {

    Long addChart(ChartAddRequest chartAddRequest);

    void deleteChart(DeleteRequest deleteRequest);

    BiResponse getChartByAI(MultipartFile file, GenChartByAIRequest request);

    BiResponse getChartByAIAsync(MultipartFile file, GenChartByAIRequest request);

    String buildUserInput(Chart chart);

    void handleChartUpdateError(Long chartId, String execMessage);

    Page<Chart> selectChartByPage(ChartQueryRequest chartQueryRequest);

    LambdaQueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    void updateChartResult(String result, Long chartId);

    BiResponse getChartByAIMq(MultipartFile file, GenChartByAIRequest request);
}
