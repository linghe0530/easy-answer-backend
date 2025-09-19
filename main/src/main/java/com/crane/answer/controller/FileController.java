package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.RandomUtil;
import com.crane.answer.common.R;
import com.crane.answer.config.CosClientConfig;
import com.crane.answer.manager.cos.CosManager;
import com.crane.answer.model.dto.file.UploadPictureRequest;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.DateUtils;
import com.crane.answer.utils.FileUtils;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.CIObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author crane
 * @date 2025.09.14 上午12:48
 * @description
 **/
@RestController
@RequestMapping("/file")
@Slf4j
@SaCheckLogin
public class FileController {


    private final CosManager cosManager;
    private final UserService userService;
    private final CosClientConfig cosClientConfig;

    public FileController(CosManager cosManager, UserService userService, CosClientConfig cosClientConfig) {
        this.cosManager = cosManager;
        this.userService = userService;
        this.cosClientConfig = cosClientConfig;
    }

    @PostMapping("/upload")
    public R<String> uploadPicture(UploadPictureRequest request, MultipartFile picture) {
        File file = null;
        PutObjectResult putObjectResult = null;
        try {
            UserResp loginInfo = userService.getLoginInfo();
            String uuid = RandomUtil.randomString(16);
            String originFileName = picture.getOriginalFilename();
            String uploadFileName = String.format("%s_%s.%s", DateUtils.formatDate(new Date()), uuid, FileUtils.getSuffix(originFileName));
            String uploadPath = String.format("/%s/%s", loginInfo.getId(), uploadFileName);
            file = File.createTempFile(uploadPath, null);
            picture.transferTo(file);
            putObjectResult = cosManager.putAppIcon(uploadPath, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            deleteTempFile(file);
        }
        CIObject object = putObjectResult.getCiUploadResult().getProcessResults().getObjectList().get(0);
        return R.ok(cosClientConfig.getHost() + "/" + object.getKey());
    }

    public void deleteTempFile(File file) {
        if (file != null) {
            boolean delete = file.delete();
            if (!delete) {
                log.error("文件删除失败:{}", file.getAbsolutePath());
            }
        }
    }
}
