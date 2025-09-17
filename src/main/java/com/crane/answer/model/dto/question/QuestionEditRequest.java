package com.crane.answer.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑题目请求
 *
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 题目内容（json格式）
     */
    @Valid
    @NotNull
    @Size(min = 1, max = 50, message = "问题内容数量必须在1-50之间")
    private List<QuestionContentDTO> questionContent;

    @Serial
    private static final long serialVersionUID = 1L;
}