package com.crane.answer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.mapper.AppMapper;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.ReviewRequest;
import com.crane.answer.model.dto.app.AppAddRequest;
import com.crane.answer.model.dto.app.AppEditRequest;
import com.crane.answer.model.dto.app.AppQueryRequest;
import com.crane.answer.model.enums.AppScoringStrategyEnum;
import com.crane.answer.model.enums.AppTypeEnum;
import com.crane.answer.model.enums.ReviewStatusEnum;
import com.crane.answer.model.enums.UserRoleEnum;
import com.crane.answer.model.po.App;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.AppResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.AppService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【app(应用信息表)】的数据库操作Service实现
 * @createDate 2025-09-10 18:40:22
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>
        implements AppService {
    @Resource
    private UserService userService;

    @Override
    public Page<AppResp> getAppRespPage(AppQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        // 查询数据库
        Page<App> page = this.page(new Page<>(current, pageSize),
                this.getQueryWrapper(request));
        List<App> appList = page.getRecords();
        Page<AppResp> pageResp = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());

        if (CollectionUtils.isEmpty(appList)) {
            return pageResp;
        }
        List<AppResp> appRespList = appList.stream().map(AppResp::objToVo).toList();
        // 1. 关联查询用户信息
        Set<Long> userIdSet = appList.stream().map(App::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        for (AppResp resp : appRespList) {

            Long userId = resp.getUserId();
            User user = null;
            if (userId != null) {
                user = userMap.get(userId);
            }
            resp.setUser(userService.getUserResp(user));
        }
        pageResp.setRecords(appRespList);
        return pageResp;
    }

    @Override
    public Long addApp(AppAddRequest request) {
        App app = new App();
        BeanUtils.copyProperties(request, app);
        // 数据校验
        this.validApp(app, true);
        // 填充默认值
        UserResp loginUser = userService.getLoginInfo();
        app.setUserId(loginUser.getId());
        if (Objects.equals(loginUser.getUserRole(), UserRoleEnum.ADMIN.getValue())) {
            app.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        } else {
            app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());

        }
        // 写入数据库
        boolean result = this.save(app);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 返回新写入的数据 id
        return app.getId();
    }

    @Override
    public AppResp getAppRespById(Long id) {
        App app = this.getById(id);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        AppResp resp = AppResp.objToVo(app);
        // 1. 关联查询用户信息
        Long userId = app.getUserId();
        if (userId == null) {
            return resp;
        }
        User user = userService.getById(userId);
        UserResp userResp = userService.getUserResp(user);
        resp.setUser(userResp);
        return resp;
    }

    @Override
    public void editApp(AppEditRequest appEditRequest) {
        App app = BeanUtil.copyProperties(appEditRequest, App.class);
        // 判断是否存在
        long id = appEditRequest.getId();
        App oldApp = this.getById(id);
        ThrowUtils.throwIf(oldApp == null, ErrorCode.NOT_FOUND_ERROR);
        UserResp loginUser = (UserResp) StpUtil.getSession().get(UserConstants.USER_LOGIN_STATE);
        // 仅本人或管理员可编辑
        if (!oldApp.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 重置审核状态
        app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        // 操作数据库
        boolean result = this.updateById(app);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
    }

    @Override
    public void validApp(App app, boolean add) {
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String appName = app.getAppName();
        String appDesc = app.getAppDesc();
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        Integer reviewStatus = app.getReviewStatus();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(appName), ErrorCode.PARAMS_ERROR, "应用名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(appDesc), ErrorCode.PARAMS_ERROR, "应用描述不能为空");
            AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
            ThrowUtils.throwIf(appTypeEnum == null, ErrorCode.PARAMS_ERROR, "应用类别非法");
            AppScoringStrategyEnum scoringStrategyEnum = AppScoringStrategyEnum.getEnumByValue(scoringStrategy);
            ThrowUtils.throwIf(scoringStrategyEnum == null, ErrorCode.PARAMS_ERROR, "应用评分策略非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(appName)) {
            ThrowUtils.throwIf(appName.length() > 80, ErrorCode.PARAMS_ERROR, "应用名称要小于 80");
        }
        if (reviewStatus != null) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            ThrowUtils.throwIf(reviewStatusEnum == null, ErrorCode.PARAMS_ERROR, "审核状态非法");
        }
    }

    @Override
    public LambdaQueryWrapper<App> getQueryWrapper(AppQueryRequest request) {
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        if (request == null) {
            return queryWrapper;
        }

        // 1. 从请求中提取参数（保持不变，建议直接用 request 调用，减少局部变量定义）
        Long id = request.getId();
        String appName = request.getAppName();
        String appDesc = request.getAppDesc();
        String appIcon = request.getAppIcon();
        Integer appType = request.getAppType();
        Integer scoringStrategy = request.getScoringStrategy();
        Integer reviewStatus = request.getReviewStatus();
        String reviewMessage = request.getReviewMessage();
        Long reviewerId = request.getReviewerId();
        Long userId = request.getUserId();
        Long notId = request.getNotId();
        String searchText = request.getSearchText();
        // 注意：分页参数（current/pageSize）和排序字段（sortField）无需放入 QueryWrapper，分页应在 service 层用 Page 对象处理

        // 2. 多字段模糊搜索（全 Lambda：用 App::getXXX 替代字符串列名）
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.and(qw ->
                    qw.like(App::getAppName, searchText) // 替代 "appName"
                            .or()
                            .like(App::getAppDesc, searchText)  // 替代 "appDesc"
            );
        }

        // 3. 单个字段模糊查询（全 Lambda）
        queryWrapper.like(StringUtils.isNotBlank(appName), App::getAppName, appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), App::getAppDesc, appDesc);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), App::getReviewMessage, reviewMessage);

        // 4. 精确查询（eq/ne，全 Lambda）
        queryWrapper.eq(StringUtils.isNotBlank(appIcon), App::getAppIcon, appIcon); // eq + 字符串非空判断
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), App::getId, notId);          // ne + 非空判断
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), App::getId, id);                // eq + 非空判断
        queryWrapper.eq(ObjectUtils.isNotEmpty(appType), App::getAppType, appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), App::getScoringStrategy, scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), App::getReviewStatus, reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), App::getReviewerId, reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), App::getUserId, userId);
        queryWrapper.orderByDesc(App::getCreateTime); // 默认按创建时间倒序
        return queryWrapper;
    }

    @Override
    public void deleteApp(DeleteRequest deleteRequest) {

    }

    @Override
    public void doAppReview(ReviewRequest request) {
        Long id = request.getId();
        Integer reviewStatus = request.getReviewStatus();
        // 校验
        // 判断是否存在
        App oldApp = this.getById(id);
        if (oldApp == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }
        // 已是该状态
        if (oldApp.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        UserResp loginUser = userService.getLoginInfo();
        App app = new App();
        app.setId(id);
        app.setReviewStatus(reviewStatus);
        app.setReviewMessage(request.getReviewMessage());
        app.setReviewerId(loginUser.getId());
        app.setReviewTime(new Date());
        boolean result = this.updateById(app);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "审核失败");
        }
    }
}




