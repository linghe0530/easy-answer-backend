package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.ReviewRequest;
import com.crane.answer.model.dto.app.AppAddRequest;
import com.crane.answer.model.dto.app.AppEditRequest;
import com.crane.answer.model.dto.app.AppQueryRequest;
import com.crane.answer.model.po.App;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.AppResp;

/**
 * @author crane
 * @description 针对表【app(应用信息表)】的数据库操作Service
 * @createDate 2025-09-10 18:40:22
 */
public interface AppService extends IService<App> {

    Page<AppResp> getAppRespPage(AppQueryRequest request);

    Long addApp(AppAddRequest request);

    AppResp getAppRespById(Long id);

    void editApp(AppEditRequest appEditRequest);

    /**
     * 验证应用程序的有效性
     *
     * @param app 需要验证的应用程序对象
     * @param add 布尔值，表示是否添加应用程序
     *            true - 表示需要添加应用程序
     *            false - 表示不需要添加应用程序
     */
    void validApp(App app, boolean add);

    LambdaQueryWrapper<App> getQueryWrapper(AppQueryRequest request);

    void deleteApp(DeleteRequest deleteRequest);

    void doAppReview(ReviewRequest request);
}
