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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author crane
 * @date 2025.09.10 下午6:18
 * @description
 **/
@ScoringStrategyConfig(appType = 1, scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) {
        Long appId = app.getId();
        //1. 根据id查询题目和题目结果
        Question question = questionService.lambdaQuery()
                .eq(Question::getAppId, appId)
                .one();

        //2.统计用户每个选择对应的属性个数
        List<ScoringResult> scoringResultList = scoringResultService.
                lambdaQuery()
                .eq(ScoringResult::getAppId, appId)
                .list();
        //3.统计用户的每个选择对应的属性个数
        HashMap<String, Integer> optionCount = new HashMap<>();
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
                    String result = option.getResult();
                    optionCount.compute(result, (k, v) -> {
                        if (v == null) {
                            return 1;
                        }
                        return v + 1;
                    });
                }
            }
        }
        //遍历各种评分结果,计算哪个结果的得分更高
        int maxScore = 0;
        ScoringResult maxScoringResult = scoringResultList.get(0);
        for (ScoringResult scoringResult : scoringResultList) {
            List<String> resultProps = JsonUtils.parseList(scoringResult.getResultProp(), String.class);
            //计算评分结果分数
            int score = resultProps.stream()
                    .mapToInt(prop -> optionCount.getOrDefault(prop, 0))
                    .sum();
            if (score > maxScore) {
                maxScore = score;
                maxScoringResult = scoringResult;
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
        userAnswer.setResultScore(maxScore);

        return userAnswer;
    }

    public static void main(String[] args) {
        HashMap<Object, Object> map = new HashMap<>();
        map.compute(1, (k, v) -> {
            System.out.println(k);
            System.out.println(v);
            return 1;
        });
        System.out.println(map);
    }
}
