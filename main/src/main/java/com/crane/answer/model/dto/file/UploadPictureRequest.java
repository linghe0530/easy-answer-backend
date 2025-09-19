package com.crane.answer.model.dto.file;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.14 上午1:07
 * @description
 **/
@Data
public class UploadPictureRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -8709557529391242846L;
    private Long appId;
}
