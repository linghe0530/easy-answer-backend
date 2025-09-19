package com.crane.answer.model.dto.picture;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author crane
 * @date 2025.09.02 下午6:58
 * @description 图片更新请求
 **/
@Data
public class PictureUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}