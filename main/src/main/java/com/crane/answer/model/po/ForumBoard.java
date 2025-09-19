package com.crane.answer.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 文章板块信息
 *
 * @TableName forum_board
 */
@TableName(value = "forum_board")
@Data
public class ForumBoard implements Serializable {
    /**
     * 板块id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父级板块id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 板块名
     */
    @TableField(value = "board_name")
    private String boardName;

    /**
     * 封面
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 描述
     */
    @TableField(value = "board_desc")
    private String boardDesc;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 0:只允许管理员发帖 1:任何人可以发帖
     */
    @TableField(value = "post_type")
    private Integer postType;


    @TableField(exist = false)
    private List<ForumBoard> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}