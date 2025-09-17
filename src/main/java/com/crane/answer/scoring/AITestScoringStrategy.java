package com.crane.answer.scoring;

import com.crane.answer.manager.ai.AiManager;
import com.crane.answer.model.dto.question.QuestionAnswerDTO;
import com.crane.answer.model.dto.question.QuestionContentDTO;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.Question;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.model.vo.QuestionResp;
import com.crane.answer.scoring.annotation.ScoringStrategyConfig;
import com.crane.answer.service.QuestionService;
import com.crane.answer.service.ScoringResultService;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author crane
 * @date 2025.09.10 下午6:26
 * @description
 **/
@ScoringStrategyConfig(appType = 1, scoringStrategy = 1)
public class AITestScoringStrategy implements ScoringStrategy {
    // 分布式锁的 key
    private static final String AI_ANSWER_LOCK = "AI_ANSWER_LOCK";

    /**
     * AI 评分结果本地缓存
     */
    private final Cache<String, String> answerCacheMap =
            Caffeine.newBuilder().
                    initialCapacity(1024)
                    // 缓存 5 分钟移除
                    .expireAfterAccess(5L, TimeUnit.MINUTES)
                    .build();
    /**
     * AI 评分系统消息
     */
    private static final String AI_TEST_SCORING_SYSTEM_MESSAGE = """
            你是一位严谨的判题专家，我会给你如下信息：
            ```
            应用名称，
            【【【应用描述】】】，
            题目和用户回答的列表：格式为 [{"title": "题目","answer": "用户回答"}]
            ```

            请你根据上述信息，按照以下步骤来对用户进行评价：
            1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）
            2. 严格按照下面的 json 格式输出评价名称和评价描述
            ```
            {"resultName": "评价名称", "resultDesc": "评价描述"}
            ```
            3. 返回格式必须为 JSON 对象""";
    @Resource
    private QuestionService questionService;
    @Resource
    private ScoringResultService scoringResultService;
    @Resource
    private AiManager aiManager;
    @Resource
    private RedissonClient redissonClient;

    @SneakyThrows
    @Override
    public UserAnswer doScore(List<String> choices, App app) {

        Long appId = app.getId();
        String choicesJson = JsonUtils.toJson(choices);
        String cacheKey = buildCacheKey(appId, choicesJson);
        String answerJson = answerCacheMap.getIfPresent(cacheKey);
        if (StringUtils.isNotBlank(answerJson)) {
            UserAnswer userAnswer = JsonUtils.parse(answerJson, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JsonUtils.toJson(choices));
            return userAnswer;
        }
        RLock lock = redissonClient.getLock(AI_ANSWER_LOCK + cacheKey);

        try {
            boolean isLocked = lock.tryLock(3, 15, TimeUnit.SECONDS);
            if (!isLocked) {
                return null;
            }
            //1.根据id查询到题目和题目结果信息 按分数降序排序
            Question question = questionService.lambdaQuery()
                    .eq(Question::getAppId, appId)
                    .one();
            QuestionResp resp = QuestionResp.objToVo(question);
            List<QuestionContentDTO> questionContent = resp.getQuestionContent();
            List<String> userAnswerList = getUserAnswerList(choices, questionContent);
            String userMessage = getAiTestScoringUserMessage(app, questionContent, userAnswerList);
            String result = aiManager.doRequest(AI_TEST_SCORING_SYSTEM_MESSAGE, userMessage);

            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            String json = result.substring(start, end + 1);
            answerCacheMap.put(cacheKey, json);
            UserAnswer userAnswer = JsonUtils.parse(json, UserAnswer.class);
            userAnswer.setAppId(appId);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JsonUtils.toJson(choices));
            return userAnswer;
        } finally {
            if (lock != null && lock.isLocked()) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 根据用户选择的选项和题目内容，获取用户答案列表
     *
     * @param choices         用户选择的选项列表
     * @param questionContent 题目内容列表
     * @return 用户答案列表，包含用户选择的选项对应的值
     */
    private static List<String> getUserAnswerList(List<String> choices, List<QuestionContentDTO> questionContent) {
        int i = 0;  // 用于遍历选项的索引
        List<String> userAnswerList = new ArrayList<>();  // 存储用户答案的列表
        for (QuestionContentDTO dto : questionContent) {  // 遍历题目内容
            List<QuestionContentDTO.Option> options = dto.getOptions();  // 获取当前题目的所有选项
            QuestionContentDTO.Option option = options.get(i);  // 获取当前索引对应的选项
            String choice = choices.get(i);  // 获取用户当前选择的选项
            if (option == null) {  // 如果选项为空，添加空字符串到答案列表
                userAnswerList.add("");
                continue;
            }
            // 如果用户选择的选项与当前选项的键相等，则添加选项的值到答案列表
            if (Objects.equals(choice, option.getKey())) {
                userAnswerList.add(option.getValue());
            }
        }
        return userAnswerList;
    }

    /**
     * AI 评分用户消息封装
     *
     * @param app
     * @param questionContentList
     * @param choices
     * @return
     */
    private String getAiTestScoringUserMessage(App app, List<QuestionContentDTO> questionContentList, List<String> choices) {
        StringBuilder userMessage = new StringBuilder();
        userMessage.append(app.getAppName()).append("\n");
        userMessage.append(app.getAppDesc()).append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContentList.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContentList.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userMessage.append(JsonUtils.toJson(questionAnswerDTOList));
        return userMessage.toString();
    }

    public String buildCacheKey(Long appId, String choices) {
        return DigestUtils.md5DigestAsHex((appId + ":" + choices).getBytes());
    }

}
