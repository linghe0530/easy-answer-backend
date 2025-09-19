package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文件信息
 * @TableName forum_article_attachment
 */
@TableName(value ="forum_article_attachment")
@Data
public class ForumArticleAttachment implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private Long articleId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 下载次数
     */
    @TableField(value = "download_count")
    private Integer downloadCount;

    /**
     * 文件路径
     */
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 文件类型
     */
    @TableField(value = "file_type")
    private Integer fileType;

    /**
     * 下载所需积分
     */
    @TableField(value = "integral")
    private Integer integral;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}