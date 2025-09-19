package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户消息
 * @TableName user_message
 */
@TableName(value ="user_message")
@Data
public class UserMessage implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收人用户ID
     */
    @TableField(value = "received_user_id")
    private Long receivedUserId;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 文章标题
     */
    @TableField(value = "article_title")
    private String articleTitle;

    /**
     * 评论ID
     */
    @TableField(value = "comment_id")
    private Long commentId;

    /**
     * 发送人用户ID
     */
    @TableField(value = "send_user_id")
    private Long sendUserId;

    /**
     * 发送人昵称
     */
    @TableField(value = "send_nick_name")
    private String sendNickName;

    /**
     * 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
     */
    @TableField(value = "message_type")
    private Integer messageType;

    /**
     * 消息内容
     */
    @TableField(value = "message_content")
    private String messageContent;

    /**
     * 1:未读 2:已读
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}