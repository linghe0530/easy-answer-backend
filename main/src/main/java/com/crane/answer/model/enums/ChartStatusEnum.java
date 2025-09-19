package com.crane.answer.model.enums;

import lombok.Getter;

/**
 * @author crane
 * @date 2025.09.16 上午11:58
 * @description
 **/
@Getter
public enum ChartStatusEnum {

    WAIT(0, "等待"),
    RUNNING(1, "运行中"),
    SUCCEED(2, "成功"),

    FAILED(3, "失败");

    /**
     * 数据库存储的字符串值
     */
    private final Integer code;

    /**
     * 状态的中文描述
     */
    private final String desc;

    ChartStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * 根据code字符串获取对应的枚举实例
     *
     * @param code 数据库中的状态字符串
     * @return 对应的枚举实例，若未找到则返回null
     */
    public static ChartStatusEnum getByCode(Integer code) {
        for (ChartStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
