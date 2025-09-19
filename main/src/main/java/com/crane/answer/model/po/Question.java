package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目信息表
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目内容（JSON格式，存储题干、选项等）
     */
    @TableField(value = "question_content")
    private String questionContent;

    /**
     * 关联应用ID（关联app表）
     */
    @TableField(value = "app_id")
    private Long appId;

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