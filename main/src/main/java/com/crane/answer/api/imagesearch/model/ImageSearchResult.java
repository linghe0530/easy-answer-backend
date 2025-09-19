package com.crane.answer.api.imagesearch.model;



import lombok.Data;

/**
 * @author crane
 * @date 2025.09.06 下午2:08
 * @description 图片搜索结果
 **/
@Data
public class ImageSearchResult {

    /**
     * 缩略图地址
     */
    private String thumbUrl;

    /**
     * 来源地址
     */
    private String fromUrl;
}
