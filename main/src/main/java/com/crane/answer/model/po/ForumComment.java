package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论
 * @TableName forum_comment
 */
@TableName(value ="forum_comment")
@Data
public class ForumComment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级评论ID
     */
    @TableField(value = "p_comment_id")
    private Long pCommentId;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 回复内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 图片
     */
    @TableField(value = "img_path")
    private String imgPath;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nickName;

    /**
     * 用户ip地址
     */
    @TableField(value = "user_ip_address")
    private String userIpAddress;

    /**
     * 回复人ID
     */
    @TableField(value = "reply_user_id")
    private Long replyUserId;

    /**
     * 回复人昵称
     */
    @TableField(value = "reply_nick_name")
    private String replyNickName;

    /**
     * 0:未置顶  1:置顶
     */
    @TableField(value = "top_type")
    private Integer topType;

    /**
     * 发布时间
     */
    @TableField(value = "post_time")
    private Date postTime;

    /**
     * good数量
     */
    @TableField(value = "good_count")
    private Integer goodCount;

    /**
     * 0:待审核  1:已审核
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}