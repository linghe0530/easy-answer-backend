package com.crane.answer.scoring;

import cn.hutool.core.annotation.AnnotationUtil;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.scoring.annotation.ScoringStrategyConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:23
 * @description
 **/
@Service
public class ScoringStrategyExecutor {
    @Resource
    private List<ScoringStrategy> scoringStrategyList;


    public UserAnswer doScore(List<String> choiceList, App app) {
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        if (appType == null || scoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYS_ERROR, "应用配置有误,未找到匹配的策略");
        }
        for (ScoringStrategy strategy : scoringStrategyList) {
            ScoringStrategyConfig annotation = AnnotationUtil.getAnnotation(strategy.getClass(), ScoringStrategyConfig.class);
            if (annotation.appType() == appType && annotation.scoringStrategy() == scoringStrategy) {
                return strategy.doScore(choiceList, app);
            }
        }
        throw new BusinessException(ErrorCode.SYS_ERROR, "应用配置有误，未找到匹配的策略");
    }
}
