package com.crane.answer.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.crane.answer.model.po.App;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description 应用视图
 */
@Data
public class AppResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -6203452712797342180L;
    /**
     * id
     */
    private Long id;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用描述
     */
    private String appDesc;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 应用类型（0-得分类，1-测评类）
     */
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserResp user;

    /**
     * 封装类转对象
     *
     * @param appVO
     * @return
     */
    public static App voToObj(AppResp appVO) {
        if (appVO == null) {
            return null;
        }
        return BeanUtil.copyProperties(appVO, App.class);

    }

    /**
     * 对象转封装类
     *
     * @param app
     * @return
     */
    public static AppResp objToVo(App app) {
        if (app == null) {
            return null;
        }
        return BeanUtil.copyProperties(app, AppResp.class);
    }
}
