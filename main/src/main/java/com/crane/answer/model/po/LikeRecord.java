package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 点赞记录
 * @TableName like_record
 */
@TableName(value ="like_record")
@Data
public class LikeRecord implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作类型 0:文章点赞 1:评论点赞
     */
    @TableField(value = "op_type")
    private Integer opType;

    /**
     * 主体ID
     */
    @TableField(value = "object_id")
    private Long objectId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 发布时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 主体作者ID
     */
    @TableField(value = "author_user_id")
    private Long authorUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}