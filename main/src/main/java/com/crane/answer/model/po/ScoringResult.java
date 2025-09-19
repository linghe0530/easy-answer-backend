package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评分结果对照表
 * @TableName scoring_result
 */
@TableName(value ="scoring_result")
@Data
public class ScoringResult implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 结果名称（如“物流师”“高级工程师”）
     */
    @TableField(value = "result_name")
    private String resultName;

    /**
     * 结果详细描述
     */
    @TableField(value = "result_desc")
    private String resultDesc;

    /**
     * 结果对应的图片URL
     */
    @TableField(value = "result_picture")
    private String resultPicture;

    /**
     * 结果属性集合（JSON格式，如["I","S","T","J"]）
     */
    @TableField(value = "result_prop")
    private String resultProp;

    /**
     * 结果命中得分范围（如80表示80分及以上命中此结果）
     */
    @TableField(value = "result_score_range")
    private Integer resultScoreRange;

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