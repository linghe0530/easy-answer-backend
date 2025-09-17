package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应用信息表
 * @TableName app
 */
@TableName(value ="app")
@Data
public class App implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 应用描述
     */
    @TableField(value = "app_desc")
    private String appDesc;

    /**
     * 应用图标URL
     */
    @TableField(value = "app_icon")
    private String appIcon;

    /**
     * 应用类型：0-得分类，1-测评类
     */
    @TableField(value = "app_type")
    private Integer appType;

    /**
     * 评分策略：0-自定义，1-AI
     */
    @TableField(value = "scoring_strategy")
    private Integer scoringStrategy;

    /**
     * 审核状态：0-待审核，1-审核通过，2-审核拒绝
     */
    @TableField(value = "review_status")
    private Integer reviewStatus;

    /**
     * 审核备注信息
     */
    @TableField(value = "review_message")
    private String reviewMessage;

    /**
     * 审核人ID（关联用户表）
     */
    @TableField(value = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @TableField(value = "review_time")
    private Date reviewTime;

    /**
     * 创建人ID（关联用户表）
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间（自动更新）
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 逻辑删除标识：0-未删除，1-已删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}