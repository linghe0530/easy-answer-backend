package com.crane.answer.api.aliyunai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.crane.answer.api.aliyunai.model.GetOutPaintingTaskResponse;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author crane
 * @date 2025.09.07 下午12:11
 * @description
 **/
@Component
@Slf4j
public class AliYunAiApi {
    @Value("${ali.apiKey}")
    private String apiKey;

    // 创建任务地址
    public static final String CREATE_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2image/out-painting";

    // 查询任务状态
    public static final String GET_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/tasks/%s";


    /**
     * 创建图像外扩任务（Out Painting）
     * 该方法用于调用AI服务进行图像外扩处理，支持异步处理方式
     *
     * @param request 包含图像外扩所需参数的请求对象，包括模型、输入参数和配置参数
     * @return CreateOutPaintingTaskResponse 包含任务创建结果的响应对象
     * @throws BusinessException 当AI服务调用失败时抛出业务异常
     */
    public CreateOutPaintingTaskResponse createOutPaintingTask(CreateOutPaintingTaskRequest request) {
        // 发送请求
        String json = JSONUtil.toJsonStr(request);
        HttpRequest httpRequest = HttpRequest.post(CREATE_OUT_PAINTING_TASK_URL)
                .header("Authorization", "Bearer " + apiKey)
                // 必须开启异步处理
                .header("X-DashScope-Async", "enable")
                .header("Content-Type", "application/json")
                .body(json);
        try (HttpResponse response = httpRequest.execute()) {
            if (!response.isOk()) {
                log.error("请求失败：{}", response.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 扩图失败");
            }
            CreateOutPaintingTaskResponse paintingTaskResponse = JsonUtils.parse(response.body(), CreateOutPaintingTaskResponse.class);
            if (paintingTaskResponse.getCode() != null) {
                String errorMessage = paintingTaskResponse.getMessage();
                log.error("创建任务失败：{}", errorMessage);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 扩图失败，" + errorMessage);
            }
            return paintingTaskResponse;
        }
    }

    /**
     * 查询创建的任务结果
     *
     * @param taskId
     * @return
     */
    public GetOutPaintingTaskResponse getOutPaintingTask(String taskId) {
        if (StringUtils.isBlank(taskId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "任务 ID 不能为空");
        }
        // 处理响应
        String url = String.format(GET_OUT_PAINTING_TASK_URL, taskId);
        try (HttpResponse httpResponse = HttpRequest.get(url)
                .header("Authorization", "Bearer " + apiKey)
                .execute()) {
            if (!httpResponse.isOk()) {
                log.error("请求异常：{}", httpResponse.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取任务结果失败");
            }
            return JsonUtils.parse(httpResponse.body(), GetOutPaintingTaskResponse.class);
        }
    }

}
