package com.crane.answer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.ai.AiManager;
import com.crane.answer.mapper.QuestionMapper;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.question.*;
import com.crane.answer.model.enums.AppTypeEnum;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.Question;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.QuestionResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.AppService;
import com.crane.answer.service.QuestionService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【question(题目信息表)】的数据库操作Service实现
 * @createDate 2025-09-10 18:40:22
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;
    @Resource
    private AppService appService;

    @Resource
    private AiManager aiManager;
    @Resource
    private Scheduler vipScheduler;

    @Override
    public String addQuestion(QuestionAddRequest questionAddRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<QuestionContentDTO> questionContent = questionAddRequest.getQuestionContent();
        question.setQuestionContent(JsonUtils.toJson(questionContent));
        Long appId = questionAddRequest.getAppId();
        App app = appService.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用不存在");
        }
        UserResp loginUser = (UserResp) StpUtil.getSession().get(UserConstants.USER_LOGIN_STATE);
        question.setUserId(loginUser.getId());
        boolean result = this.save(question);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long questionId = question.getId();
        if (questionId == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return questionId.toString();
    }

    @Override
    public void editQuestion(QuestionEditRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        List<QuestionContentDTO> questionContent = request.getQuestionContent();
        question.setQuestionContent(JsonUtils.toJson(questionContent));
        UserResp loginUser = (UserResp) StpUtil.getSession().get(UserConstants.USER_LOGIN_STATE);
        // 判断是否存在
        long id = request.getId();
        Question oldQuestion = this.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.updateById(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public Page<QuestionResp> listQuestionRespPage(QuestionQueryRequest request) {
        Page<Question> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), this.getQueryWrapper(request));

        List<Question> questionList = page.getRecords();
        Page<QuestionResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return pageResp;
        }
        // 对象列表 => 封装对象列表
        List<QuestionResp> questionVOList = questionList.stream().map(QuestionResp::objToVo).collect(Collectors.toList());
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, User> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.toMap(User::getId, v -> v));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId);
            }
            questionVO.setUser(userService.getUserResp(user));
        });

        pageResp.setRecords(questionVOList);
        return pageResp;
    }

    @Override
    public LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从请求对象中提取参数
        Long id = questionQueryRequest.getId();
        String questionContent = questionQueryRequest.getQuestionContent();
        Long appId = questionQueryRequest.getAppId();
        Long userId = questionQueryRequest.getUserId();
        Long notId = questionQueryRequest.getNotId();

        // 模糊查询（使用Lambda方法引用替代字符串字段名）
        queryWrapper.like(StringUtils.isNotBlank(questionContent), Question::getQuestionContent, questionContent);

        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), Question::getId, notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), Question::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), Question::getAppId, appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), Question::getUserId, userId);
        return queryWrapper;
    }

    @Override
    public QuestionResp getQuestionRespById(Long id) {
        return null;
    }

    @Override
    public void deleteQuestion(DeleteRequest deleteRequest) {

    }

    //AI 生成题目功能
    private static final String GENERATE_QUESTION_SYSTEM_MESSAGE = """
            你是一位严谨的出题专家，我会给你如下信息：
            ```
            应用名称，
            【【【应用描述】】】，
            应用类别，
            要生成的题目数，
            每个题目的选项数
            ```

            请你根据上述信息，按照以下步骤来出题：
            1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复
            2. 严格按照下面的 json 格式输出题目和选项
            ```
            [{"options":[{"value":"选项内容","key":"A"},{"value":"","key":"B"}],"title":"题目标题"}]
            ```
            title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容
            3. 检查题目是否包含序号，若包含序号则去除序号
            4. 返回的题目列表格式必须为 JSON 数组""";

    /**
     * 生成题目的用户消息
     *
     * @param app
     * @param questionNumber
     * @param optionNumber
     * @return
     */
    private String getGenerateQuestionUserMessage(App app, int questionNumber, int optionNumber) {
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        if (appTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用类型错误");
        }
        return app.getAppName() + "\n" + app.getAppDesc() + "\n" + appTypeEnum.getText() + "类" + "\n" + questionNumber + "\n" + optionNumber;
    }

    @Override
    public List<QuestionContentDTO> aiGenerateQuestion(AiGenerateQuestionRequest request) {
        Long appId = request.getAppId();
        int questionNum = request.getQuestionNum();
        int optionNum = request.getOptionNum();
        App app = appService.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用不存在");
        }
        String userMessage = getGenerateQuestionUserMessage(app, questionNum, optionNum);
        String result = aiManager.doRequest(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
        int start = result.indexOf("[");
        int end = result.lastIndexOf("]");
        String json = result.substring(start, end + 1);
        return JsonUtils.parseList(json, QuestionContentDTO.class);
    }

    @Override
    public SseEmitter aiGenerateQuestionStream(AiGenerateQuestionRequest request) {
        Long appId = request.getAppId();
        int questionNum = request.getQuestionNum();
        int optionNum = request.getOptionNum();
        App app = appService.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用不存在");
        }
        String userMessage = getGenerateQuestionUserMessage(app, questionNum, optionNum);
        SseEmitter emitter = new SseEmitter(0L);
        Flux<String> flux = aiManager.doRequestStream(GENERATE_QUESTION_SYSTEM_MESSAGE, userMessage);
        // 3. 订阅流式响应，手动发送SSE事件
        AtomicInteger counter = new AtomicInteger(0);
        StringBuilder jsonBuilder = new StringBuilder();
        Scheduler scheduler = Schedulers.single();
        UserResp loginInfo = userService.getLoginInfo();
        boolean admin = userService.isAdmin(loginInfo);
        if (admin) {
            scheduler = vipScheduler;
        }

        flux
                // 指定处理线程池
                .subscribeOn(scheduler)
                // 去除所有空白字符
                .map(message -> message.replaceAll("\\s", ""))
                // 过滤空消息
                .filter(StringUtils::isNotBlank)
                // 将字符串拆分为单个字符流
                .flatMap(message -> {
                    List<Character> chars = new ArrayList<>();
                    for (char c : message.toCharArray()) {
                        chars.add(c);
                    }
                    return Flux.fromIterable(chars);
                })
                // 处理每个字符，构建完整JSON对象
                .doOnNext(c -> {
                    // 遇到左括号，计数器加1
                    if (c == '{') {
                        counter.incrementAndGet();
                    }

                    // 当计数器大于0时，说明处于JSON对象内部，需要拼接字符
                    if (counter.get() > 0) {
                        jsonBuilder.append(c);
                    }

                    // 遇到右括号，计数器减1
                    if (c == '}') {
                        counter.decrementAndGet();

                        // 计数器归0，说明捕获到完整的JSON对象
                        if (counter.get() == 0) {
                            // 通过SSE发送完整JSON
                            try {
                                emitter.send(jsonBuilder.toString());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            // 重置构建器，准备下一个JSON对象
                            jsonBuilder.setLength(0);
                        }
                    }
                })
                // 错误处理
                .doOnError(e -> log.error("SSE处理错误", e))
                // 完成时关闭SSE连接
                .doOnComplete(emitter::complete)
                // 订阅执行
                .subscribe();

        return emitter;
    }
}




