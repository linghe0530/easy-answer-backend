package com.crane.answer.manager.mq.consumer;

import com.crane.answer.constants.AiConstants;
import com.crane.answer.constants.MqConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.ai.AiManager;
import com.crane.answer.model.enums.ChartStatusEnum;
import com.crane.answer.model.po.Chart;
import com.crane.answer.service.ChartService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author crane
 * @date 2025.09.16 下午4:10
 * @description
 **/
@Component
@Slf4j
public class ChartConsumer {
    @Resource
    private ChartService chartService;
    @Resource
    private AiManager aiManager;

    @RabbitListener(queues = MqConstants.CHART_QUEUE)
    public void onMessage(String message) {
        long id = Long.parseLong(message);
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图表不存在");
        }
        chart.setStatus(ChartStatusEnum.RUNNING.getCode());
        chart.setId(chart.getId());
        boolean updateSuccess = chartService.updateById(chart);
        if (!updateSuccess) {
            chartService.handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        String result = aiManager.doRequest(AiConstants.prompt, chartService.buildUserInput(chart));
        chartService.updateChartResult(result, id);
    }
}
