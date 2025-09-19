package com.crane.answer.model.dto.picture;



import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.06 下午8:05
 * @description 图片批量编辑请求
 **/
@Data
public class PictureEditByBatchRequest implements Serializable {

    /**
     * 图片 id 列表
     */
    private List<Long> pictureIdList;


    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 命名规则
     */
    private String nameRule;

    @Serial
    private static final long serialVersionUID = 1L;
}
