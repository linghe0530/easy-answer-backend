package com.crane.answer.manager.cos;

import com.crane.answer.config.CosClientConfig;
import com.crane.answer.constants.FileConstants;
import com.crane.answer.utils.FileUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 下午4:49
 * @description
 **/
@Component
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private COSClient cosClient;


    /**
     * 上传并解析图片
     */
    public PutObjectResult putAppIcon(String key, File file)
            throws CosClientException {
        PutObjectRequest request = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        PicOperations picOperations = new PicOperations();
        //1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        List<PicOperations.Rule> rules = new ArrayList<>();
        //图片压缩 转为webp格式
        String webpName = FileUtils.mainName(key) + FileConstants.WEBP;
        PicOperations.Rule compressRule = new PicOperations.Rule();
        compressRule.setBucket(cosClientConfig.getBucket());
        compressRule.setFileId(webpName);
        compressRule.setRule("imageMogr2/format/webp");
        rules.add(compressRule);
        picOperations.setRules(rules);
        request.setPicOperations(picOperations);
        return cosClient.putObject(request);
    }

    public PutObjectResult putPicObject(String key, File file)
            throws CosClientException {
        PutObjectRequest request = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        PicOperations picOperations = new PicOperations();
        //1 表示返回原图信息
        picOperations.setIsPicInfo(1);
        List<PicOperations.Rule> rules = new ArrayList<>();
        //图片压缩 转为webp格式
        String webpName = FileUtils.mainName(key) + FileConstants.WEBP;
        PicOperations.Rule compressRule = new PicOperations.Rule();
        compressRule.setBucket(cosClientConfig.getBucket());
        compressRule.setFileId(webpName);
        compressRule.setRule("imageMogr2/format/webp");
        //仅对大于30kb的图片进行生成缩略图
        if (file.length() > 30 * 1024) {
            PicOperations.Rule thumbnilRule = new PicOperations.Rule();
            thumbnilRule.setBucket(cosClientConfig.getBucket());
            String thumbnailKey = FileUtils.mainName(key) + "_thumbnail." + FileUtils.getSuffix(key);
            thumbnilRule.setFileId(thumbnailKey);
            thumbnilRule.setRule(String.format("imageMogr2/thumbnail/%sx%s>", 256, 256));
            rules.add(thumbnilRule);
        }
        rules.add(compressRule);
        picOperations.setRules(rules);
        request.setPicOperations(picOperations);
        return cosClient.putObject(request);
    }

    public void deletePicObject(String key)
            throws CosClientException {
        cosClient.deleteObject(cosClientConfig.getBucket(), key);


    }
}
