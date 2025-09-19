package com.crane.answer.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.api.aliyunai.AliYunAiApi;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskRequest;
import com.crane.answer.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.cos.CosManager;
import com.crane.answer.manager.upload.FilePictureUpload;
import com.crane.answer.manager.upload.PictureUploadTemplate;
import com.crane.answer.manager.upload.UrlPictureUpload;
import com.crane.answer.mapper.PictureMapper;
import com.crane.answer.model.dto.file.UploadPictureResult;
import com.crane.answer.model.dto.picture.*;
import com.crane.answer.model.enums.ReviewStatusEnum;
import com.crane.answer.model.po.Picture;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.PictureResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.PictureService;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author crane
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2025-09-16 19:12:24
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {


    @Resource
    private UserService userService;
    @Resource
    @Lazy
    private PictureService pictureService;
    @Resource
    private PictureMapper pictureMapper;
    @Resource
    private FilePictureUpload filePictureUpload;
    @Resource
    private UrlPictureUpload urlPictureUpload;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private CosManager cosManager;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private AliYunAiApi aliYunAiApi;
    /**
     * 本地图片缓存
     */


    @Override
    public void checkPictureAuth(Picture picture, User loginUser) {
        Long loginUserId = loginUser.getId();
        if (!picture.getUserId().equals(loginUserId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Override
    public PictureResp uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest) {
        UserResp loginUser = userService.getLoginInfo();
        //判断新增还是更新
        Long pictureId = pictureUploadRequest.getId();
        //更新图片
        if (pictureId != null) {
            Picture picture = pictureMapper.selectById(pictureId);
            if (picture == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "图片不存在");
            }
        }
        //上传图片
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        //上传图片得到图片信息
        PictureUploadTemplate pictureUploadTemplate = filePictureUpload;
        //区分数据源
        if (inputSource instanceof String) {
            pictureUploadTemplate = urlPictureUpload;
        }
        UploadPictureResult result = pictureUploadTemplate.uploadPicture(inputSource, uploadPathPrefix);
        // 构造要入库的图片信息
        Picture picture = new Picture();
        picture.setUrl(result.getUrl());
        picture.setThumbnailUrl(result.getThumbnailUrl());
        // 支持外层传递图片名称
        String picName = result.getPicName();
        if (StringUtils.isNotBlank(pictureUploadRequest.getPicName())) {
            picName = pictureUploadRequest.getPicName();
        }
        picture.setName(picName);
        picture.setPicSize(result.getPicSize());
        picture.setPicWidth(result.getPicWidth());
        picture.setPicHeight(result.getPicHeight());
        picture.setPicScale(result.getPicScale());
        picture.setPicFormat(result.getPicFormat());
        picture.setUserId(loginUser.getId());
        picture.setPicColor(result.getPicColor());
        picture.setId(pictureId);
        //补充审核参数
        this.fillReviewParams(picture, loginUser);

        boolean isSuccess = pictureMapper.insertOrUpdate(picture);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片上传失败");
        }
        return PictureResp.objToVo(picture);
    }

    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(picture == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时，id 不能为空，有参数则校验
        ThrowUtils.throwIf(ObjUtil.isNull(id), ErrorCode.PARAMS_ERROR, "id 不能为空");
        // 如果传递了 url，才校验
        if (StringUtils.isNotBlank(url)) {
            ThrowUtils.throwIf(url.length() > 1024, ErrorCode.PARAMS_ERROR, "url 过长");
        }
        if (StringUtils.isNotBlank(introduction)) {
            ThrowUtils.throwIf(introduction.length() > 800, ErrorCode.PARAMS_ERROR, "简介过长");
        }
    }

    @Override
    public PictureResp getPictureResp(Picture picture) {
        // 对象转封装类
        PictureResp response = PictureResp.objToVo(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserResp userResp = userService.getUserResp(user);
            response.setUser(userResp);
        }
        return response;
    }

    /**
     * 分页获取图片封装
     */
    @Override
    public Page<PictureResp> getPicturePage(PictureQueryRequest request) {
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        Page<Picture> picturePage = this.page(new Page<>(current, pageSize), pictureService.getQueryWrapper(request));
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureResp> pictureVOPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        if (CollUtils.isEmpty(pictureList)) {
            return pictureVOPage;
        }
        // 对象列表 => 封装对象列表
        List<PictureResp> pictureVOList = pictureList.stream().map(PictureResp::objToVo).collect(Collectors.toList());
        // 1. 关联查询用户信息
        // 1,2,3,4
        Set<Long> userIdSet = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        // 1 => user1, 2 => user2
        Map<Long, User> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.toMap(User::getId, v -> v));
        // 2. 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId);
            }
            pictureVO.setUser(userService.getUserResp(user));
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        String searchText = pictureQueryRequest.getSearchText();
        Long userId = pictureQueryRequest.getUserId();
        Integer reviewStatus = pictureQueryRequest.getReviewStatus();
        String reviewMessage = pictureQueryRequest.getReviewMessage();
        Long reviewerId = pictureQueryRequest.getReviewerId();
        Date startEditTime = pictureQueryRequest.getStartEditTime();
        Date endEditTime = pictureQueryRequest.getEndEditTime();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        // 从多字段中搜索
        if (StrUtil.isNotBlank(searchText)) {
            // 需要拼接查询条件
            // and (name like "%xxx%" or introduction like "%xxx%")
            queryWrapper.and(qw -> qw.like("name", searchText).or().like("introduction", searchText));
        }
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "user_id", userId);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(introduction), "introduction", introduction);
        queryWrapper.like(StringUtils.isNotBlank(picFormat), "pic_format", picFormat);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "review_message", reviewMessage);
        queryWrapper.eq(StringUtils.isNotBlank(category), "category", category);
        queryWrapper.eq(ObjUtil.isNotEmpty(picWidth), "pic_width", picWidth);
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "pic_height", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "pic_size", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "pic_scale", picScale);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewStatus), "review_status", reviewStatus);
        queryWrapper.eq(ObjUtil.isNotEmpty(reviewerId), "reviewer_id", reviewerId);
        // >= startEditTime
        queryWrapper.ge(ObjUtil.isNotEmpty(startEditTime), "edit_time", startEditTime);
        // < endEditTime
        queryWrapper.lt(ObjUtil.isNotEmpty(endEditTime), "edit_time", endEditTime);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tags)) {
            /* and (tag like "%\"Java\"%" and like "%\"Python\"%") */
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 排序
        queryWrapper.orderBy(StringUtils.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public void deletePicture(Long pictureId) {
        UserResp loginUser = userService.getLoginInfo();
        Picture oldPicture = this.getById(pictureId);
        if (oldPicture == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        if (!Objects.equals(loginUser.getId(), oldPicture.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限删除图片");
        }
        boolean success = this.removeById(pictureId);
        if (!success) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除图片失败");
        }
        this.clearPictureFile(oldPicture);
    }

    @Override
    public void editPicture(PictureEditRequest pictureEditRequest) {
        UserResp loginUser = userService.getLoginInfo();
        long id = pictureEditRequest.getId();
        Picture oldPicture = this.getById(id);
        if (oldPicture == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        if (!Objects.equals(loginUser.getId(), oldPicture.getUserId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限编辑图片");
        }
        Picture updatePicture = new Picture();
        BeanUtils.copyProperties(pictureEditRequest, updatePicture);
        updatePicture.setTags(JsonUtils.toJson(pictureEditRequest.getTags()));
        updatePicture.setUpdateTime(new Date());
        this.validPicture(updatePicture);
        // 补充审核参数
        this.fillReviewParams(updatePicture, loginUser);
        // 操作数据库
        boolean result = this.updateById(updatePicture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void doPictureReview(PictureReviewRequest request) {
        UserResp loginUser = userService.getLoginInfo();
        //校验参数
        Long id = request.getId();
        Integer reviewStatus = request.getReviewStatus();
        String reviewMessage = request.getReviewMessage();
        ReviewStatusEnum statusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
        if (statusEnum == null || ReviewStatusEnum.REVIEWING.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断 图片是否存在
        Picture picture = pictureMapper.selectById(id);
        ThrowUtils.throwIf(picture == null, ErrorCode.NOT_FOUND_ERROR);

        //校验审核状态是否重复
        if (picture.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复操作");
        }
        //数据库操作
        Picture update = new Picture();
        update.setId(id);
        update.setReviewMessage(reviewMessage);
        update.setReviewStatus(reviewStatus);
        update.setReviewerId(loginUser.getId());
        update.setReviewTime(new Date());
        int effect = pictureMapper.updateById(update);
        ThrowUtils.throwIf(effect <= 0, ErrorCode.OPERATION_ERROR);
    }


    @Override
    public void fillReviewParams(Picture picture, UserResp loginUser) {
        if (userService.isAdmin(loginUser)) {
            picture.setReviewStatus(ReviewStatusEnum.PASS.getValue());
            picture.setReviewerId(loginUser.getId());
            picture.setReviewMessage("管理员自动审核");
            picture.setReviewTime(new Date());
        } else {
            picture.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
        }
    }

    @Override
    public Integer uploadPictureBatch(PictureUploadBatchRequest request) {
        String searchText = request.getSearchText();
        Integer count = request.getCount();
        String namePrefix = request.getNamePrefix();

        //校验参数
        ThrowUtils.throwIf(count > 20, ErrorCode.PARAMS_ERROR, "最多获取30张图片");
        if (StringUtils.isBlank(namePrefix)) {
            namePrefix = searchText;
        }
        String fetchUrl = String.format("https://cn.bing.com/images/async?q=%s&mmasync=1", searchText);
        Document document;
        try {
            document = Jsoup.connect(fetchUrl).get();
        } catch (IOException e) {
            log.error("获取页面数据失败");
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取页面数据失败");
        }
        //解析内容
        Element div = document.getElementsByClass("dgControl").first();
        if (div == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取元素失败");
        }
        Elements imgElementList = div.select("img.mimg");
        //遍历元素，依次处理上传图片
        int uploadCount = 0;
        for (Element element : imgElementList) {
            String fileUrl = element.attr("src");
            if (StringUtils.isBlank(fileUrl)) {
                log.info("当前链接为空,已跳过:{}", fileUrl);
                continue;
            }
            //处理图片地址
            int markIndex = fileUrl.indexOf("?");
            if (markIndex > -1) {
                fileUrl = fileUrl.substring(0, markIndex);
            }
            PictureUploadRequest pictureUploadRequest = new PictureUploadRequest();
            pictureUploadRequest.setFileUrl(fileUrl);
            pictureUploadRequest.setPicName(namePrefix + (uploadCount + 1));
            try {
                this.uploadPicture(fileUrl, pictureUploadRequest);
                log.info("图片上传成功,url={}", fileUrl);
                uploadCount++;
            } catch (Exception e) {
                log.info("图片上传失败,url={}", fileUrl);
                continue;
            }
            if (uploadCount >= count) {
                break;
            }
        }

        return uploadCount;
    }


    @Async
    @Override
    public void clearPictureFile(Picture picture) {
        //
        String url = picture.getUrl();
        Long count = this.lambdaQuery().eq(Picture::getUrl, url).count();
        if (count > 1) {
            return;
        }
        cosManager.deletePicObject(url);
        String thumbnailUrl = picture.getThumbnailUrl();
        if (StringUtils.isNotBlank(thumbnailUrl)) {
            cosManager.deletePicObject(thumbnailUrl);
        }
    }

    @Override
    public List<PictureResp> searchPictureByColor(String picColor) {
        UserResp loginUser = userService.getLoginInfo();
        //校验参数
        ThrowUtils.throwIf(StringUtils.isBlank(picColor), ErrorCode.PARAMS_ERROR);
        List<Picture> pictureList = this.lambdaQuery().isNotNull(Picture::getPicColor).list();
        if (CollUtils.isEmpty(pictureList)) {
            return new ArrayList<>();
        }
        Color targetColor = Color.decode(picColor);
        return pictureList.stream().sorted(Comparator.comparingDouble(picture -> {
            String picColorStr = picture.getPicColor();
            if (StringUtils.isBlank(picColorStr)) {
                return Double.MAX_VALUE;
            }
            Color pictureColor = Color.decode(picColorStr);
            return -ColorSimilarUtils.calculateSimilarity(targetColor, pictureColor);
        })).limit(12).map(PictureResp::objToVo).toList();
    }

    @Override
    public void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest) {
        UserResp loginUser = userService.getLoginInfo();

        // 1. 获取和校验参数
        List<Long> pictureIdList = pictureEditByBatchRequest.getPictureIdList();
        String category = pictureEditByBatchRequest.getCategory();
        List<String> tags = pictureEditByBatchRequest.getTags();
        ThrowUtils.throwIf(CollUtil.isEmpty(pictureIdList), ErrorCode.PARAMS_ERROR);

        //查询指定图片
        List<Picture> pictureList = this.lambdaQuery().select(Picture::getId).in(Picture::getId, pictureIdList).list();
        if (pictureList.isEmpty()) {
            return;
        }
        //更新分类和标签
        pictureList.forEach(pic -> {
            if (StringUtils.isNotBlank(category)) {
                pic.setCategory(category);
            }
            if (CollUtils.isNotEmpty(tags)) {
                pic.setTags(JsonUtils.toJson(tags));
            }
        });
        String nameRule = pictureEditByBatchRequest.getNameRule();
        fillPictureWithNameRole(pictureList, nameRule);
        boolean result = pictureService.updateBatchById(pictureList);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "批量编辑失败");
    }

    @Override
    public CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest request) {
        UserResp loginInfo = userService.getLoginInfo();
        Long pictureId = request.getPictureId();
        Picture picture = Optional.ofNullable(pictureMapper.selectById(pictureId)).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图片不存在"));
        CreateOutPaintingTaskRequest createOutPaintingTaskRequest = new CreateOutPaintingTaskRequest();
        CreateOutPaintingTaskRequest.Input input = new CreateOutPaintingTaskRequest.Input();
        input.setImageUrl(picture.getUrl());
        createOutPaintingTaskRequest.setInput(input);
        createOutPaintingTaskRequest.setParameters(request.getParameters());
        return aliYunAiApi.createOutPaintingTask(createOutPaintingTaskRequest);
    }


    /**
     * 根据名称规则为图片列表填充名称
     *
     * @param pictureList 图片列表，需要填充名称和角色的Picture对象集合
     * @param nameRule    名称规则，用于生成图片名称的规则字符串
     */
    private void fillPictureWithNameRole(List<Picture> pictureList, String nameRule) {
        if (StringUtils.isBlank(nameRule) || CollUtils.isEmpty(pictureList)) {
            return;
        }
        int count = 1;
        try {
            for (Picture picture : pictureList) {
                String picName = nameRule.replaceAll("\\{序号}", String.valueOf(count++));
                picture.setName(picName);
            }
        } catch (Exception e) {
            log.error("名称解析错误", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "名称解析错误");
        }
    }

}




