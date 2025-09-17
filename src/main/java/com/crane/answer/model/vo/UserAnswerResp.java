package com.crane.answer.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.crane.answer.model.po.UserAnswer;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description 应用视图
 */
@Data
public class UserAnswerResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 6149026018022211233L;
    /**
     * 答案id
     */
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    /**
     * 评分结果 id
     */
    private Long resultId;

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 结果图标
     */
    private String resultPicture;

    /**
     * 得分
     */
    private Integer resultScore;

    /**
     * 用户 id
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
     * @param resp
     * @return
     */
    public static UserAnswer voToObj(UserAnswerResp resp) {
        if (resp == null) {
            return null;
        }
        UserAnswer userAnswer = BeanUtil.copyProperties(resp, UserAnswer.class);
        userAnswer.setChoices(JSONUtil.toJsonStr(resp.getChoices()));
        return userAnswer;
    }

    /**
     * 对象转封装类
     *
     * @param userAnswer
     * @return
     */
    public static UserAnswerResp objToVo(UserAnswer userAnswer) {
        if (userAnswer == null) {
            return null;
        }
        UserAnswerResp resp = BeanUtil.copyProperties(userAnswer, UserAnswerResp.class);
        resp.setChoices(JSONUtil.toList(userAnswer.getChoices(), String.class));
        return resp;
    }
}
