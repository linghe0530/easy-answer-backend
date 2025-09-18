package com.crane.answer.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.mapper.UserAnswerMapper;
import com.crane.answer.model.dto.userAnswer.UserAnswerAddRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerQueryRequest;
import com.crane.answer.model.enums.ReviewStatusEnum;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.User;
import com.crane.answer.model.po.UserAnswer;
import com.crane.answer.model.vo.UserAnswerResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.AppService;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【user_answer(用户答题记录表（主表）)】的数据库操作Service实现
 * @createDate 2025-09-10 18:40:23
 */
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
        implements UserAnswerService {
    @Resource
    private AppService appService;
    @Resource
    private UserService userService;
    @Resource
    private ScoringStrategyExecutor strategyExecutor;

    @Override
    public Long addUserAnswer(UserAnswerAddRequest request) {
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(request, userAnswer);
        List<String> choices = request.getChoices();
        userAnswer.setChoices(JsonUtils.toJson(choices));
        //数据校验
        Long appId = request.getAppId();
        App app = appService.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (ReviewStatusEnum.PASS.getValue() != app.getReviewStatus()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "应用未通过审核");
        }
        UserResp loginInfo = userService.getLoginInfo();
        userAnswer.setUserId(loginInfo.getId());
        boolean isSuccess = this.save(userAnswer);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long userAnswerId = userAnswer.getId();
        UserAnswer userAnswerWithResult = strategyExecutor.doScore(choices, app);
        userAnswerWithResult.setAppId(null); // 不设置分表会报错
        userAnswerWithResult.setId(userAnswerId);
        this.updateById(userAnswerWithResult);
        return userAnswerId;
    }

    @Override
    public UserAnswerResp getUserAnswerRespById(Long id) {
        // 查询数据库
        UserAnswer userAnswer = this.getById(id);
        if (userAnswer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 获取封装类
        UserAnswerResp resp = UserAnswerResp.objToVo(userAnswer);
        // 1. 关联查询用户信息
        Long userId = userAnswer.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "无权限");
        }
        UserResp loginInfo = userService.getLoginInfo();
        if (!Objects.equals(loginInfo.getId(), userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限");
        }

        User user = userService.getById(userId);
        UserResp userResp = userService.getUserResp(user);
        resp.setUser(userResp);
        return resp;
    }

    @Override
    public Page<UserAnswerResp> getUserAnswerRespPage(UserAnswerQueryRequest request) {

        int current = request.getCurrent();
        int pageSize = request.getPageSize();

        Page<UserAnswer> page = this.page(new Page<>(current, pageSize), this.getQueryWrapper(request));

        List<UserAnswer> userAnswerList = page.getRecords();
        Page<UserAnswerResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(userAnswerList)) {
            return pageResp;
        }
        // 对象列表 => 封装对象列表
        List<UserAnswerResp> userAnswerVOList = userAnswerList.stream().map(UserAnswerResp::objToVo).collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = userAnswerList.stream().map(UserAnswer::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, v -> v));
        // 填充信息
        userAnswerVOList.forEach(userAnswerVO -> {
            Long userId = userAnswerVO.getUserId();
            User user = null;
            if (userMap.containsKey(userId)) {
                user = userMap.get(userId);
            }
            userAnswerVO.setUser(userService.getUserResp(user));
        });

        pageResp.setRecords(userAnswerVOList);
        return pageResp;
    }

    @Override
    public LambdaQueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest) {
        // 核心变更：使用 LambdaQueryWrapper，指定实体类 UserAnswer
        LambdaQueryWrapper<UserAnswer> queryWrapper = new LambdaQueryWrapper<>();
        if (userAnswerQueryRequest == null) {
            return queryWrapper; // LambdaQueryWrapper 是 QueryWrapper 的子类，可直接返回
        }

        // 从请求对象中提取参数（逻辑不变）
        Long id = userAnswerQueryRequest.getId();
        Long appId = userAnswerQueryRequest.getAppId();
        Integer appType = userAnswerQueryRequest.getAppType();
        Integer scoringStrategy = userAnswerQueryRequest.getScoringStrategy();
        String choices = userAnswerQueryRequest.getChoices();
        Long resultId = userAnswerQueryRequest.getResultId();
        String resultName = userAnswerQueryRequest.getResultName();
        String resultDesc = userAnswerQueryRequest.getResultDesc();
        String resultPicture = userAnswerQueryRequest.getResultPicture();
        Integer resultScore = userAnswerQueryRequest.getResultScore();
        Long userId = userAnswerQueryRequest.getUserId();
        Long notId = userAnswerQueryRequest.getNotId();
        String searchText = userAnswerQueryRequest.getSearchText();
        String sortField = userAnswerQueryRequest.getSortField();
        String sortOrder = userAnswerQueryRequest.getSortOrder();

        // 1. 多字段搜索（Lambda 方法引用替代字符串字段名）
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw
                    .like(UserAnswer::getResultName, searchText)  // 替代 "resultName"
                    .or()
                    .like(UserAnswer::getResultDesc, searchText)  // 替代 "resultDesc"
            );
        }

        // 2. 模糊查询（全 Lambda 写法，消除硬编码）
        queryWrapper.like(StringUtils.isNotBlank(choices), UserAnswer::getChoices, choices);
        queryWrapper.like(StringUtils.isNotBlank(resultName), UserAnswer::getResultName, resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), UserAnswer::getResultDesc, resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultPicture), UserAnswer::getResultPicture, resultPicture);

        // 3. 精确查询（同理，用方法引用替代字符串）
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), UserAnswer::getId, notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), UserAnswer::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), UserAnswer::getUserId, userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultId), UserAnswer::getResultId, resultId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), UserAnswer::getAppId, appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), UserAnswer::getAppType, appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScore), UserAnswer::getResultScore, resultScore);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), UserAnswer::getScoringStrategy, scoringStrategy);
        queryWrapper.orderByDesc(UserAnswer::getCreateTime);

        return queryWrapper;
    }

    private void validUserAnswer(UserAnswer userAnswer, boolean add) {
        ThrowUtils.throwIf(userAnswer == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long appId = userAnswer.getAppId();
        Long id = userAnswer.getId();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
            ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR, "id 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (appId != null) {
            App app = appService.getById(appId);
            ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        }
    }
}




