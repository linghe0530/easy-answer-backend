package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章信息
 * @TableName forum_article
 */
@TableName(value ="forum_article")
@Data
public class ForumArticle implements Serializable {
    /**
     * 文章ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 板块ID
     */
    @TableField(value = "board_id")
    private Long boardId;

    /**
     * 板块名称
     */
    @TableField(value = "board_name")
    private String boardName;

    /**
     * 父级板块ID
     */
    @TableField(value = "p_board_id")
    private Integer pBoardId;

    /**
     * 父板块名称
     */
    @TableField(value = "p_board_name")
    private String pBoardName;

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
     * 最后登录ip地址
     */
    @TableField(value = "user_ip_address")
    private String userIpAddress;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 封面
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * markdown内容
     */
    @TableField(value = "markdown_content")
    private String markdownContent;

    /**
     * 0:富文本编辑器 1:markdown编辑器
     */
    @TableField(value = "editor_type")
    private Integer editorType;

    /**
     * 摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 发布时间
     */
    @TableField(value = "post_time")
    private Date postTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 阅读数量
     */
    @TableField(value = "read_count")
    private Integer readCount;

    /**
     * 点赞数
     */
    @TableField(value = "good_count")
    private Integer goodCount;

    /**
     * 评论数
     */
    @TableField(value = "comment_count")
    private Integer commentCount;

    /**
     * 0未置顶  1:已置顶
     */
    @TableField(value = "top_type")
    private Integer topType;

    /**
     * 0:没有附件  1:有附件
     */
    @TableField(value = "attachment_type")
    private Integer attachmentType;

    /**
     * -1已删除 0:待审核  1:已审核 
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}