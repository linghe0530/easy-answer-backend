package com.crane.answer.scoring;

import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.enums.AppScoringStrategyEnum;
import com.crane.answer.model.enums.AppTypeEnum;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.UserAnswer;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:08
 * @description
 **/
@Component
@Deprecated
public class ScoringStrategyContext {

    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;
    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;

    public UserAnswer doScore(List<String> choiceList, App app) {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        AppScoringStrategyEnum appScoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(app.getScoringStrategy());
        if (appTypeEnum == null || appScoringStrategyEnum == null) {
            throw new BusinessException(ErrorCode.SYS_ERROR, "应用配置有误,未找到匹配的策略");
        }
        switch (appTypeEnum) {
            case SCORE -> {
                switch (appScoringStrategyEnum) {
                    case CUSTOM -> customScoreScoringStrategy.doScore(choiceList, app);
                    case AI -> {
                    }
                }
            }
            case TEST -> {
                switch (appScoringStrategyEnum) {
                    case CUSTOM -> customTestScoringStrategy.doScore(choiceList, app);
                    case AI -> {
                    }
                }
            }
        }
        throw new BusinessException(ErrorCode.SYS_ERROR, "应用配置有误，未找到匹配的策略");
    }
}
