package com.crane.answer.model.dto.chart;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建请求
 *
 * @author crane
 * @date 2025.09.14 上午1:07
 * @description
 */
@Data
public class ChartAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    @Serial
    private static final long serialVersionUID = 1L;
}