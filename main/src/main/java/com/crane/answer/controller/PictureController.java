package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.api.aliyunai.AliYunAiApi;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.crane.answer.api.aliyunai.model.GetOutPaintingTaskResponse;
import com.crane.answer.api.imagesearch.ImageSearchApiFacade;
import com.crane.answer.api.imagesearch.model.BaiduSimilarImageResponse;
import com.crane.answer.common.R;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.limiter.RedisLimiterManager;
import com.crane.answer.model.dto.picture.*;
import com.crane.answer.model.enums.ReviewStatusEnum;
import com.crane.answer.model.po.Picture;
import com.crane.answer.model.vo.PictureResp;
import com.crane.answer.model.vo.PictureTagCategoryResponse;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.PictureService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.16 下午7:07
 * @description
 **/
@RestController
@RequestMapping("/picture")
@Valid
public class PictureController {
    @Resource
    private PictureService pictureService;
    @Resource
    private UserService userService;
    @Resource
    private AliYunAiApi aliYunAiApi;

    @Resource
    private RedisLimiterManager redisLimiterManager;

    @PostMapping("/upload")
    @SaCheckLogin
    public R<PictureResp> uploadPicture(@RequestPart("file") MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest) {
        PictureResp resp = pictureService.uploadPicture(multipartFile, pictureUploadRequest);
        return R.ok(resp);
    }

    /**
     * 根据url上传图片
     */
    @PostMapping("/upload/url")
    @SaCheckLogin
    public R<PictureResp> uploadPictureByUrl(@RequestBody PictureUploadRequest pictureUploadRequest) {
        PictureResp resp = pictureService.uploadPicture(pictureUploadRequest.getFileUrl(), pictureUploadRequest);
        return R.ok(resp);
    }

    /**
     * 编辑图片（给用户使用）
     */
    @PostMapping("/edit")
    @SaCheckLogin
    public R<Boolean> editPicture(@RequestBody @Valid PictureEditRequest request) {
        pictureService.editPicture(request);
        return R.ok(true);
    }

    @PostMapping("/delete")
    @SaCheckLogin
    public R<Boolean> deletePicture(@RequestBody @Valid PictureDeleteRequest deleteRequest) {
        pictureService.deletePicture(deleteRequest.getId());
        return R.ok(true);
    }

    @GetMapping("/get/resp")
    public R<PictureResp> getPictureRespById(PictureGetRequest pictureGetRequest) {
        Long id = pictureGetRequest.getId();
        // 查询数据库
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR);
        PictureResp response = pictureService.getPictureResp(picture);
        // 获取封装类
        return R.ok(response);
    }


    /**
     * 分页获取图片列表（封装类）
     */
    @PostMapping("/list/page/resp")
    public R<Page<PictureResp>> listPictureRespPage(@RequestBody PictureQueryRequest request) {
        long size = request.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        request.setReviewStatus(ReviewStatusEnum.PASS.getValue());
        Page<PictureResp> pageResp = pictureService.getPicturePage(request);
        // 获取封装类
        return R.ok(pageResp);
    }


    @GetMapping("/tag_category")
    public R<PictureTagCategoryResponse> listPictureTagCategory() {
        PictureTagCategoryResponse pictureTagCategory = new PictureTagCategoryResponse();
        List<String> tagList = Arrays.asList("热门", "搞笑", "生活", "高清", "艺术", "校园", "背景", "简历", "创意");
        List<String> categoryList = Arrays.asList("模板", "电商", "表情包", "素材", "海报");
        pictureTagCategory.setTagList(tagList);
        pictureTagCategory.setCategoryList(categoryList);
        return R.ok(pictureTagCategory);
    }


    @PostMapping("/search/picture")
    @SaCheckLogin
    public R<List<BaiduSimilarImageResponse.ResponseData.ImageItem>> searchPictureByPicture(@RequestBody @Valid SearchPictureByPictureRequest request) {
        UserResp loginInfo = userService.getLoginInfo();
        if (!userService.isAdmin(loginInfo)) {

            redisLimiterManager.doRateLimit("answer:searchPictureByPicture" + loginInfo.getId(), 2);
        }
        Long pictureId = request.getPictureId();
        Picture picture = pictureService.getById(pictureId);
        if (picture == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        return R.ok(ImageSearchApiFacade.searchImage(picture.getUrl()));
    }

    @PostMapping("/search/color")
    public R<List<PictureResp>> searchPictureByColor(@RequestBody SearchPictureByColorRequest request) {
        String picColor = request.getPicColor();
        return R.ok(pictureService.searchPictureByColor(picColor));
    }


    /**
     * 批量编辑图片
     */
    @PostMapping("/edit/batch")
    @SaCheckLogin
    public R<Boolean> editPictureByBatch(@RequestBody PictureEditByBatchRequest request) {
        pictureService.editPictureByBatch(request);
        return R.ok(true);
    }

    /**
     * 创建 AI 扩图任务
     */
    @PostMapping("/out_painting/create_task")
    @SaCheckLogin
    public R<CreateOutPaintingTaskResponse> createPictureOutPaintingTask(@RequestBody @Valid CreatePictureOutPaintingTaskRequest request) {
        CreateOutPaintingTaskResponse response = pictureService.createPictureOutPaintingTask(request);
        return R.ok(response);
    }

    /**
     * 查询 AI 扩图任务
     */
    @GetMapping("/out_painting/get_task")
    @SaCheckLogin
    public R<GetOutPaintingTaskResponse> getPictureOutPaintingTask(@NotBlank(message = "查询任务不能为空") String taskId) {
        GetOutPaintingTaskResponse task = aliYunAiApi.getOutPaintingTask(taskId);
        return R.ok(task);
    }
}
