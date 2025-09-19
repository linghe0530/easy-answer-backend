package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户答题记录表（主表）
 * @TableName user_answer
 */
@TableName(value ="user_answer")
@Data
public class UserAnswer implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联应用ID（关联app表）
     */
    @TableField(value = "app_id")
    private Long appId;

    /**
     * 应用类型：0-得分类，1-角色测评类（冗余字段，避免关联查询）
     */
    @TableField(value = "app_type")
    private Integer appType;

    /**
     * 评分策略：0-自定义，1-AI（冗余字段）
     */
    @TableField(value = "scoring_strategy")
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON数组，存储每题选择的选项）
     */
    @TableField(value = "choices")
    private String choices;

    /**
     * 关联评分结果ID（关联scoring_result表，无结果则为NULL）
     */
    @TableField(value = "result_id")
    private Long resultId;

    /**
     * 结果名称（冗余字段，避免关联查询）
     */
    @TableField(value = "result_name")
    private String resultName;

    /**
     * 结果描述（冗余字段）
     */
    @TableField(value = "result_desc")
    private String resultDesc;

    /**
     * 结果图片URL（冗余字段）
     */
    @TableField(value = "result_picture")
    private String resultPicture;

    /**
     * 用户最终得分（无得分则为NULL）
     */
    @TableField(value = "result_score")
    private Integer resultScore;

    /**
     * 答题用户ID（关联用户表）
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 答题提交时间
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