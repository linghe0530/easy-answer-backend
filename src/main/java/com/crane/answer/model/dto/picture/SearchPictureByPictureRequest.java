package com.crane.answer.model.dto.picture;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.06 下午3:51
 * @description 以图搜图请求
 **/

@Data
public class SearchPictureByPictureRequest implements Serializable {

    /**
     * 图片 id
     */
    @NotNull(message = "图片 id 不能为空")
    private Long pictureId;

    @Serial
    private static final long serialVersionUID = 1L;
}
