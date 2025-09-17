package com.crane.answer.api.aliyunai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crane
 * @date 2025.09.07 下午12:05
 * @description 查询扩图任务响应类
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOutPaintingTaskResponse {

    /**
     * 请求唯一标识
     * 反序列化支持 "request_id" 别名，序列化仍使用 "requestId"
     */
    @JsonAlias("request_id")
    private String requestId;

    /**
     * 输出信息
     * 反序列化支持 "output" 别名（与原字段名一致，可省略）
     */
    @JsonAlias("output")
    private Output output;
    /**
     * 图像统计信息。
     */
    @JsonAlias("usage")
    private Usage usage;

    @Data
    public static class Usage {
        /**
         * 模型生成图片的数量。
         */
        @JsonAlias("image_count")
        private Integer imageCount;
    }

    /**
     * 表示任务的输出信息
     */
    @Data
    public static class Output {

        /**
         * 任务 ID
         */
        @JsonAlias("task_id")
        private String taskId;

        /**
         * 任务状态
         */
        @JsonAlias("task_status")
        private String taskStatus;

        /**
         * 提交时间
         */
        @JsonAlias("submit_time")
        private String submitTime;

        /**
         * 调度时间
         */
        @JsonAlias("scheduled_time")
        private String scheduledTime;

        /**
         * 结束时间
         */
        @JsonAlias("end_time")
        private String endTime;

        /**
         * 输出图像的 URL
         */
        @JsonAlias("output_image_url")
        private String outputImageUrl;

        /**
         * 接口错误码
         */
        @JsonAlias("code")
        private String code;

        /**
         * 接口错误信息
         */
        @JsonAlias("message")
        private String message;

        /**
         * 任务指标信息
         */
        @JsonAlias("task_metrics")
        private TaskMetrics taskMetrics;
    }

    /**
     * 表示任务的统计信息
     */
    @Data
    public static class TaskMetrics {

        /**
         * 总任务数
         */
        @JsonAlias("total")
        private Integer total;

        /**
         * 成功任务数
         */
        @JsonAlias("succeeded")
        private Integer succeeded;

        /**
         * 失败任务数
         */
        @JsonAlias("failed")
        private Integer failed;
    }
}
