package com.crane.answer.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.02 下午5:08
 * @description
 **/
@Data
public class PictureUploadBatchRequest implements Serializable {


    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 抓取数量
     */
    private Integer count;

    /**
     * 图片名称前缀
     */
    private String namePrefix;

    @Serial
    private static final long serialVersionUID = 1L;
}