package com.crane.answer.model.dto.app;

import com.crane.answer.manager.valid.EnumTypeValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建应用请求
 */
@Data
public class AppAddRequest implements Serializable {

    /**
     * 应用名
     */
    @NotBlank(message = "应用名称不能为空")
    @Length(min = 3, max = 80, message = "应用名称长度必须在 3-80 个字符之间")
    private String appName;

    /**
     * 应用描述
     */
    @NotBlank(message = "应用描述不能为空")
    private String appDesc;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    @EnumTypeValid(values = {0, 1}, message = "无效应用类型")
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    @EnumTypeValid(values = {0, 1}, message = "无效评分策略")
    private Integer scoringStrategy;

    @Serial
    private static final long serialVersionUID = 1L;
}