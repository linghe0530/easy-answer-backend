package com.crane.answer.scoring;

import com.crane.answer.model.dto.question.QuestionContentDTO;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.Question;
import com.crane.answer.model.po.ScoringResult;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.model.vo.QuestionResp;
import com.crane.answer.scoring.annotation.ScoringStrategyConfig;
import com.crane.answer.service.QuestionService;
import com.crane.answer.service.ScoringResultService;
import com.crane.answer.utils.JsonUtils;
import jakarta.annotation.Resource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author crane
 * @date 2025.09.10 下午6:26
 * @description
 **/
@ScoringStrategyConfig(appType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        Long appId = app.getId();
        //1.根据id查询到题目和题目结果信息 按分数降序排序
        Question question = questionService.lambdaQuery()
                .eq(Question::getAppId, appId)
                .one();
        List<ScoringResult> scoringResultList = scoringResultService.
                lambdaQuery()
                .eq(ScoringResult::getAppId, appId)
                .orderByDesc(ScoringResult::getResultScoreRange)
                .list();
        //2.统计用户总得分
        int totalCount = 0;
        QuestionResp resp = QuestionResp.objToVo(question);
        List<QuestionContentDTO> questionContent = resp.getQuestionContent();
        //遍历题目列表
        for (QuestionContentDTO questionContentDTO : questionContent) {
            //遍历选项列表
            for (String answer : choices) {
                // 如果答案和选项的key匹配
                for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                    if (!Objects.equals(option.getKey(), answer)) {
                        continue;
                    }
                    int score = Optional.of(option.getScore())
                            .orElse(0);
                    totalCount += score;

                }
            }
        }
        //3.遍历得分结果找到第一个用户分数大于得分值的结果作为最终结果
        ScoringResult maxScoringResult = scoringResultList.get(0);
        for (ScoringResult scoringResult : scoringResultList) {
            if (totalCount >= scoringResult.getResultScoreRange()) {
                maxScoringResult = scoringResult;
                break;
            }
        }
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(appId);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JsonUtils.toJson(choices));
        userAnswer.setResultId(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalCount);

        return userAnswer;
    }
}
