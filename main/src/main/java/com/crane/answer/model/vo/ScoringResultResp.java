package com.crane.answer.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.crane.answer.model.po.ScoringResult;
import com.crane.answer.utils.JsonUtils;
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
public class ScoringResultResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -6203452712797342180L;
    /**
     * id
     */
    private Long id;

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
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
    private Long appId;

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
     * @param scoringResultVO
     * @return
     */
    public static ScoringResult voToObj(ScoringResultResp scoringResultVO) {
        if (scoringResultVO == null) {
            return null;
        }
        ScoringResult scoringResult = BeanUtil.copyProperties(scoringResultVO, ScoringResult.class);
        scoringResult.setResultProp(JsonUtils.toJson(scoringResultVO.getResultProp()));
        return scoringResult;
    }

    /**
     * 对象转封装类
     *
     * @param scoringResult
     * @return
     */
    public static ScoringResultResp objToVo(ScoringResult scoringResult) {
        if (scoringResult == null) {
            return null;
        }
        ScoringResultResp resp = BeanUtil.copyProperties(scoringResult, ScoringResultResp.class);
        resp.setResultProp(JsonUtils.parseList(scoringResult.getResultProp(), String.class));
        return resp;
    }
}
