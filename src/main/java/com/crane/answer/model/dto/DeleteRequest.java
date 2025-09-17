package com.crane.answer.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.11 下午7:39
 * @description
 **/
@Data
public class DeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -5978061165462746439L;
    @NotNull(message = "id不能为空")
    private Long id;

}
