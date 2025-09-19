package com.crane.answer.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 下午8:03
 * @description
 **/
@Data
public class PictureTagCategoryResponse {
    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 分类列表
     */
    private List<String> categoryList;
}
