package com.crane.answer.scoring;

import com.crane.answer.model.po.App;
import com.crane.answer.model.po.UserAnswer;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午6:11
 * @description 策略接口
 **/
public interface ScoringStrategy {
    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app);
}
