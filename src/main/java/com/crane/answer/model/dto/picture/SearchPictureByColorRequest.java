package com.crane.answer.model.dto.picture;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.06 下午3:51
 * @description 按照颜色搜索图片请求
 **/
@Data
public class SearchPictureByColorRequest implements Serializable {

    /**
     * 图片主色调
     */
    private String picColor;


    @Serial
    private static final long serialVersionUID = 1L;
}