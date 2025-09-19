package com.crane.answer.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.15 下午4:31
 * @description
 **/
@Data
public class BiResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 7693756577471052252L;
    private Long chartId;
    private String genChart;
    private String genResult;
}
