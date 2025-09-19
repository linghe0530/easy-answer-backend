package com.crane.answer.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.mapper.ScoringResultMapper;
import com.crane.answer.model.dto.scoringResult.ScoringResultAddRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultEditRequest;
import com.crane.answer.model.dto.scoringResult.ScoringResultQueryRequest;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.ScoringResult;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.ScoringResultResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.AppService;
import com.crane.answer.service.ScoringResultService;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【scoring_result(评分结果对照表)】的数据库操作Service实现
 * @createDate 2025-09-10 18:40:23
 */
@Service
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult>
        implements ScoringResultService {
    @Resource
    private UserService userService;
    @Resource
    private AppService appService;

    @Override
    public Page<ScoringResultResp> listScoringResultRespPage(ScoringResultQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        Page<ScoringResult> page = this.page(new Page<>(current, pageSize), getQueryWrapper(request));
        List<ScoringResult> scoringResultList = page.getRecords();
        Page<ScoringResultResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollUtil.isEmpty(scoringResultList)) {
            return pageResp;
        }
        // 对象列表 => 封装对象列表
        List<ScoringResultResp> scoringResultVOList = scoringResultList.stream()
                .map(ScoringResultResp::objToVo)
                .collect(Collectors.toList());

        // 1. 关联查询用户信息
        Set<Long> userIdSet = scoringResultList.stream().map(ScoringResult::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.listByIds(userIdSet)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        // 填充信息
        scoringResultVOList.forEach(resultResp -> {
            Long userId = resultResp.getUserId();
            User user = null;
            if (userMap.containsKey(userId)) {
                user = userMap.get(userId);
            }
            resultResp.setUser(userService.getUserResp(user));
        });

        pageResp.setRecords(scoringResultVOList);
        return pageResp;
    }

    @Override
    public LambdaQueryWrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest) {
        LambdaQueryWrapper<ScoringResult> queryWrapper = new LambdaQueryWrapper<>();
        if (scoringResultQueryRequest == null) {
            return queryWrapper;
        }

        // 从请求对象中提取参数
        Long id = scoringResultQueryRequest.getId();
        String resultName = scoringResultQueryRequest.getResultName();
        String resultDesc = scoringResultQueryRequest.getResultDesc();
        String resultPicture = scoringResultQueryRequest.getResultPicture();
        String resultProp = scoringResultQueryRequest.getResultProp();
        Integer resultScoreRange = scoringResultQueryRequest.getResultScoreRange();
        Long userId = scoringResultQueryRequest.getUserId();
        Long appId = scoringResultQueryRequest.getAppId();
        Long notId = scoringResultQueryRequest.getNotId();
        String searchText = scoringResultQueryRequest.getSearchText();


        // 多字段搜索（使用Lambda方法引用替代字符串列名）
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw
                    .like(ScoringResult::getResultName, searchText)  // 替代 "resultName"
                    .or()
                    .like(ScoringResult::getResultDesc, searchText)  // 替代 "resultDesc"
            );
        }

        // 模糊查询（全Lambda写法）
        queryWrapper.like(StringUtils.isNotBlank(resultName), ScoringResult::getResultName, resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), ScoringResult::getResultDesc, resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultProp), ScoringResult::getResultProp, resultProp);

        // 精确查询（全Lambda写法）
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), ScoringResult::getId, notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), ScoringResult::getId, id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), ScoringResult::getUserId, userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(appId), ScoringResult::getAppId, appId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScoreRange), ScoringResult::getResultScoreRange, resultScoreRange);
        queryWrapper.eq(StringUtils.isNotBlank(resultPicture), ScoringResult::getResultPicture, resultPicture);
        queryWrapper.orderByDesc(ScoringResult::getCreateTime);
        return queryWrapper;
    }

    @Override
    public Long addScoringResult(ScoringResultAddRequest request) {
        Long appId = request.getAppId();
        App app = appService.getById(appId);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(request, scoringResult);
        List<String> resultProp = request.getResultProp();
        scoringResult.setResultProp(JSONUtil.toJsonStr(resultProp));
        // 数据校验
        this.validScoringResult(scoringResult, true);
        // 填充默认值
        UserResp loginUser = userService.getLoginInfo();
        scoringResult.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = this.save(scoringResult);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 返回新写入的数据 id
        return scoringResult.getId();
    }

    @Override
    public void editScoringResult(ScoringResultEditRequest request) {
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(request, scoringResult);
        List<String> resultProp = request.getResultProp();
        scoringResult.setResultProp(JsonUtils.toJson(resultProp));
        // 数据校验
        this.validScoringResult(scoringResult, false);
        UserResp loginUser = userService.getLoginInfo();
        // 判断是否存在
        long id = request.getId();
        ScoringResult oldScoringResult = this.getById(id);
        if (oldScoringResult == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可编辑
        if (!oldScoringResult.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.updateById(scoringResult);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public void validScoringResult(ScoringResult scoringResult, boolean add) {
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String resultName = scoringResult.getResultName();
        Long appId = scoringResult.getAppId();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(resultName), ErrorCode.PARAMS_ERROR, "结果名称不能为空");
            ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "appId 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(resultName)) {
            ThrowUtils.throwIf(resultName.length() > 128, ErrorCode.PARAMS_ERROR, "结果名称不能超过 128");
        }
        // 补充校验规则
        if (appId != null) {
            App app = appService.getById(appId);
            ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR, "应用不存在");
        }
    }

    @Override
    public void deleteScoringResult(Long id) {
        UserResp user = userService.getLoginInfo();
        // 判断是否存在
        ScoringResult oldScoringResult = this.getById(id);
        if (oldScoringResult == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "评分结果不存在");
        }
        // 仅本人或管理员可删除
        if (!oldScoringResult.getUserId().equals(user.getId()) && !userService.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }
}




