package com.crane.answer.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.crane.answer.model.po.ForumBoard;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.19 上午12:22
 * @description
 **/
@Data
public class ForumBoardResp implements Serializable {
    @Serial
    private static final long serialVersionUID = -2852339023551417312L;
    private Long id;

    /**
     * 父级板块id
     */
    private Long parentId;

    /**
     * 板块名
     */
    private String boardName;

    /**
     * 封面
     */
    private String cover;

    /**
     * 描述
     */
    private String boardDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 0:只允许管理员发帖 1:任何人可以发帖
     */
    private Integer postType;


    private List<ForumBoard> children;
}
