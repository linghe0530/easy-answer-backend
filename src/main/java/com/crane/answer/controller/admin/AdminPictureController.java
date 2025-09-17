package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.picture.PictureQueryRequest;
import com.crane.answer.model.dto.picture.PictureReviewRequest;
import com.crane.answer.model.dto.picture.PictureUpdateRequest;
import com.crane.answer.model.dto.picture.PictureUploadBatchRequest;
import com.crane.answer.model.po.Picture;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.PictureService;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/v3/picture")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminPictureController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private PictureService pictureService;

    /**
     * 上传图片（可重新上传）
     */
    @PostMapping("/upload/batch")
    public R<Integer> uploadPictureBatch(@RequestBody PictureUploadBatchRequest request) {
        Integer count = pictureService.uploadPictureBatch(request);
        return R.ok(count);
    }

    @PostMapping("/review")
    public R<Boolean> reviewPicture(@RequestBody @Valid PictureReviewRequest request) {
        pictureService.doPictureReview(request);
        return R.ok(true);
    }

    @PostMapping("/list/page")
    public R<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<Picture> picturePage = pictureService.page(new Page<>(current, size),
                pictureService.getQueryWrapper(request));
        return R.ok(picturePage);
    }

    @GetMapping("/get")
    public R<Picture> getPictureById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Picture picture = pictureService.getById(id);
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return R.ok(picture);
    }

    @PostMapping("/update")
    public R<Boolean> updatePicture(@RequestBody PictureUpdateRequest pictureUpdateRequest,
                                    HttpServletRequest request) {
//        if (pictureUpdateRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 将实体类和 DTO 进行转换
//        Picture picture = BeanUtil.copyProperties(pictureUpdateRequest, Picture.class);
//        // 注意将 list 转为 string
//        picture.setTags(JsonUtils.toJson(pictureUpdateRequest.getTags()));
//        // 数据校验
//        pictureService.validPicture(picture);
//        // 判断是否存在
//        long id = pictureUpdateRequest.getId();
//        Picture oldPicture = pictureService.getById(id);
//        ThrowUtils.throwIf(oldPicture == null, ErrorCode.NOT_FOUND_ERROR);
//        // 补充审核参数
//        User loginUser = userService.getLoginUser(request);
//        pictureService.fillReviewParams(oldPicture, loginUser);
//        // 操作数据库
//        boolean result = pictureService.updateById(picture);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }
}
