package com.crane.answer.api.imagesearch.model;

import lombok.Data;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.06 下午3:17
 * @description 百度图片相似搜索响应顶层实体类
 **/
@Data
public class BaiduSimilarImageResponse {
    /**
     * 状态码（0 通常表示成功）
     */
    private Integer status;

    /**
     * 响应数据主体
     */
    private ResponseData data;

    // ====================== 内部类：响应数据主体 ======================
    @Data
    public static class ResponseData {
        /**
         * 图片列表总数
         */
        private Integer length;

        /**
         * 图片信息列表
         */
        private List<ImageItem> list;

        /**
         * 异步加载文本的 URL
         */
        private String ajaxTextUrl;

        /**
         * 图片 URL（可能为空，视接口返回而定）
         */
        private String imageUrl;

        // ====================== 内部类：单张图片信息 ======================
        @Data
        public static class ImageItem {
            /**
             * 内容签名（可能用于唯一标识内容）
             */
            private String contsign;

            /**
             * 图片高度
             */
            private Integer height;

            /**
             * 图片宽度
             */
            private Integer width;

            /**
             * 缩略图 URL
             */
            private String thumbUrl;

            /**
             * hover 状态图片 URL（可能为空）
             */
            private String hoverUrl;

            /**
             * 图片来源 URL（原始页面地址）
             */
            private String fromUrl;

            /**
             * 图片详情页 URL（百度图谱跳转地址）
             */
            private String objUrl;

            /**
             * 图片在列表中的索引
             */
            private Integer index;

            /**
             * 图片所属页码
             */
            private Integer page;
        }
    }
}