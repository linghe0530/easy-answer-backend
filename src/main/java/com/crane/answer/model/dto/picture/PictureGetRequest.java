package com.crane.answer.model.dto.picture;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.02 下午6:58
 * @description 图片编辑请求
 **/
@Data
public class PictureGetRequest implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;


    @Serial
    private static final long serialVersionUID = 1L;
}