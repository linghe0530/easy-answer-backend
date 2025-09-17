package com.crane.answer.model.dto.picture;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 下午6:58
 * @description 图片编辑请求
 **/
@Data
public class PictureEditRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "图片id不能为空")
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    @NotBlank(message = "分类不能为空")
    private String category;

    /**
     * 标签
     */
    @NotEmpty(message = "标签不能为空")
    @Size(max = 10, message = "标签数量不能超过10个")
    private List<String> tags;

    @Serial
    private static final long serialVersionUID = 1L;
}