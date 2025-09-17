package com.crane.answer.model.dto.scoringResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 创建评分结果请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description
 */
@Data
public class ScoringResultAddRequest implements Serializable {

    /**
     * 结果名称，如物流师
     */
    @NotBlank(message = "结果名称不能为空")
    @Length(max = 100, message = "结果名称过长")
    private String resultName;

    /**
     * 结果描述
     */
    @NotBlank(message = "结果描述不能为空")
    private String resultDesc;

    /**
     * 结果图片
     */
    private String resultPicture;

    /**
     * 结果属性集合 JSON，如 [I,S,T,J]
     */
    private List<String> resultProp;

    /**
     * 结果得分范围，如 80，表示 80及以上的分数命中此结果
     */
    private Integer resultScoreRange;

    /**
     * 应用 id
     */
    @NotNull(message = "无效应用")
    private Long appId;

    private static final long serialVersionUID = 1L;
}