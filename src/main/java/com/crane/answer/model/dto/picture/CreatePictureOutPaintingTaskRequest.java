package com.crane.answer.model.dto.picture;


import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.07 下午12:44
 * @description 创建扩图任务请求
 **/
@Data
public class CreatePictureOutPaintingTaskRequest implements Serializable {

    /**
     * 图片 id
     */
    @NotNull
    private Long pictureId;

    /**
     * 扩图参数
     */
    private CreateOutPaintingTaskRequest.Parameters parameters;

    @Serial
    private static final long serialVersionUID = 1L;
}
