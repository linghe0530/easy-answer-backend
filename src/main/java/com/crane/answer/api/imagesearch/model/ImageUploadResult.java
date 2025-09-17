package com.crane.answer.api.imagesearch.model;


import lombok.Data;

/**
 * @author crane
 * @date 2025.09.06 下午2:08
 * @description 图片搜索结果
 **/
@Data
public class ImageUploadResult {
    /**
     * 状态码，0表示成功
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private DataDTO data;

    /**
     * 数据对象内部类
     */
    @Data
    public static class DataDTO {
        /**
         * 跳转URL
         */
        private String url;

        /**
         * 签名信息
         */
        private String sign;
    }
}
