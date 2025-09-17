package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 图表信息表
 *
 * @TableName chart
 */
@TableName(value = "chart")
@Data
public class Chart implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分析目标
     */
    @TableField(value = "goal")
    private String goal;

    /**
     * 图表名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 图表数据
     */
    @TableField(value = "chart_data")
    private String chartData;

    /**
     * 图表类型
     */
    @TableField(value = "chart_type")
    private String chartType;

    /**
     * 生成的图表数据
     */
    @TableField(value = "gen_chart")
    private String genChart;

    /**
     * 生成的分析结论
     */
    @TableField(value = "gen_result")
    private String genResult;

    /**
     * 状态(0:wait-等待, 1:running-运行中, 2:succeed-成功, 3:failed-失败)
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 执行信息
     */
    @TableField(value = "exec_message")
    private String execMessage;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}