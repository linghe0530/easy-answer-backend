package com.crane.answer.model.dto.chart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.14 上午1:07
 * @description 文件上传请求
 */
@Data
public class GenChartByAIRequest implements Serializable {

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(min = 2, max = 100, message = "名称长度在2-100之间")
    private String name;

    /**
     * 分析目标
     */
    @NotBlank(message = "分析目标不能为空")
    private String goal;

    /**
     * 图表类型
     */
    @NotBlank(message = "图表类型不能为空")
    private String chartType;

    @Serial
    private static final long serialVersionUID = 1L;
}