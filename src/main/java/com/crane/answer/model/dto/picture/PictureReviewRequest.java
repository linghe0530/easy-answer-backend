package com.crane.answer.model.dto.picture;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.03 下午2:27
 * @description 图片审核请求
 **/
@Data
public class PictureReviewRequest implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;

    /**
     * 审核状态：0-待审核; 1-通过; 2-拒绝
     */
    @NotNull
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    @Serial
    private static final long serialVersionUID = 1L;
}