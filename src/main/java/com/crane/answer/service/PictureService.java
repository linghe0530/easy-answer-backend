package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.crane.answer.model.dto.picture.*;
import com.crane.answer.model.po.Picture;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.PictureResp;
import com.crane.answer.model.vo.UserResp;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author linghe01
 * @description 针对表【picture(图片)】的数据库操作Service
 * @createDate 2025-09-16 19:12:24
 */
public interface PictureService extends IService<Picture> {
    /**
     * @param picture
     * @param loginUser
     */
    void checkPictureAuth(Picture picture, User loginUser);

    /**
     * 上传图片
     *
     * @param inputSource          文件输入源
     * @param pictureUploadRequest
     * @return
     */
    PictureResp uploadPicture(Object inputSource,
                              PictureUploadRequest pictureUploadRequest);

    /**
     * 验证图片的有效性和合法性
     *
     * @param picture 需要验证的Picture对象
     */
    void validPicture(Picture picture);

    PictureResp getPictureResp(Picture picture);

    Page<PictureResp> getPicturePage(PictureQueryRequest request);

    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    void deletePicture(Long id);

    void editPicture(PictureEditRequest pictureEditRequest);


    /**
     * 图片审核
     *
     * @param request
     */
    void doPictureReview(PictureReviewRequest request);

    void fillReviewParams(Picture picture, UserResp loginUser);

    Integer uploadPictureBatch(PictureUploadBatchRequest request);


    void clearPictureFile(Picture picture);

    /**
     * 根据颜色搜索图片
     *
     * @param picColor
     * @return
     */
    List<PictureResp> searchPictureByColor( String picColor);


    /**
     * 批量编辑图片方法
     *
     * @param pictureEditByBatchRequest 包含批量编辑图片参数的请求对象
     * @param loginUser                 执行当前操作的用户信息
     */
    void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest);

    /**
     * 创建图像外绘(OutPainting)任务的方法
     *
     * @param createPictureOutPaintingTaskRequest 创建图像外绘任务的请求参数对象，包含任务相关的所有必要信息
     * @param loginUser                           当前登录用户对象，用于验证用户身份和权限
     * @return
     */
    CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest);
}
