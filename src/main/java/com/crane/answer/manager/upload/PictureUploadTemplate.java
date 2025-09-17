package com.crane.answer.manager.upload;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.crane.answer.config.CosClientConfig;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.manager.cos.CosManager;
import com.crane.answer.model.dto.file.UploadPictureResult;
import com.crane.answer.utils.CollUtils;
import com.crane.answer.utils.DateUtils;
import com.crane.answer.utils.FileUtils;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.ProcessResults;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 下午4:49
 * @description
 **/
@Slf4j
public abstract class PictureUploadTemplate {
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private CosManager cosManager;


    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        //校验图片
        validPicture(inputSource);
        //图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originFileName = getOriginalFilename(inputSource);
        String uploadFileName = String.format("%s_%s.%s", DateUtils.formatDate(new Date()), uuid, FileUtils.getSuffix(originFileName));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFileName);
        File file = null;
        try {
            //创建临时文件
            file = File.createTempFile(uploadPath, null);
            processFile(inputSource, file);
            //上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPicObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //获取图片处理结果
            ProcessResults processResults = putObjectResult.getCiUploadResult().getProcessResults();
            List<CIObject> objectList = processResults.getObjectList();
            if (CollUtils.isNotEmpty(objectList)) {
                //获取压缩后的文件信息
                CIObject compressObject = objectList.get(0);
                //缩略图默认等于压缩图
                CIObject thumbnailObject = compressObject;
                if (objectList.size() > 1) {
                    thumbnailObject = objectList.get(1);
                }
                return buildResult(originFileName, compressObject, thumbnailObject, imageInfo);
            }
            return buildResult(imageInfo, uploadPath, originFileName, file);
        } catch (Exception e) {
            log.error("图片上传到对象存储失败:", e);
            throw new BusinessException(ErrorCode.SYS_ERROR, "上传失败");
        } finally {
            //删除临时文件
            deleteTempFile(file);
        }
    }

    /**
     * @param originFileName
     * @param compressObject
     * @param thumbnailObject 缩略图
     * @param imageInfo
     * @return
     */
    private UploadPictureResult buildResult(String originFileName, CIObject compressObject, CIObject thumbnailObject, ImageInfo imageInfo) {
        String format = compressObject.getFormat();
        int width = compressObject.getWidth();
        int height = compressObject.getHeight();
        double scale = NumberUtil.round((double) width / height, 2).doubleValue();
        UploadPictureResult result = new UploadPictureResult();
        result.setUrl(cosClientConfig.getHost() + "/" + compressObject.getKey());
        result.setPicName(FileUtils.mainName(originFileName));
        result.setPicSize(compressObject.getSize().longValue());
        result.setPicWidth(width);
        result.setPicHeight(height);
        result.setPicScale(scale);
        result.setPicFormat(format);
        result.setThumbnailUrl(cosClientConfig.getHost() + "/" + thumbnailObject.getKey());
        result.setPicColor(imageInfo.getAve());
        return result;
    }

    private UploadPictureResult buildResult(ImageInfo imageInfo, String uploadPath, String originFileName, File file) {
        String format = imageInfo.getFormat();
        int width = imageInfo.getWidth();
        int height = imageInfo.getHeight();
        double scale = NumberUtil.round((double) width / height, 2).doubleValue();
        UploadPictureResult result = new UploadPictureResult();
        result.setUrl(cosClientConfig.getHost() + uploadPath);
        result.setPicName(FileUtils.mainName(originFileName));
        result.setPicSize(FileUtils.size(file));
        result.setPicWidth(width);
        result.setPicHeight(height);
        result.setPicScale(scale);
        result.setPicFormat(format);
        result.setPicColor(imageInfo.getAve());

        return result;
    }

    public void deleteTempFile(File file) {
        if (file != null) {
            boolean delete = file.delete();
            if (!delete) {
                log.error("文件删除失败:{}", file.getAbsolutePath());
            }
        }
    }

    protected abstract String getOriginalFilename(Object inputSource);

    protected abstract void validPicture(Object inputSource);

    /**
     * 处理文件来源
     *
     * @param inputSource
     * @param file
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;


}
