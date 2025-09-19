package com.crane.answer.common;

import lombok.Data;

/**
 * 分页请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description 题目视图
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = "desc";

}
