package com.crane.answer.manager.upload;

import com.crane.answer.constants.FileConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author crane
 * @date 2025.09.03 下午4:49
 * @description
 **/
@Service
public class FilePictureUpload extends PictureUploadTemplate {
    @Override
    protected String getOriginalFilename(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        return multipartFile.getOriginalFilename();
    }

    @Override
    protected void validPicture(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        if (multipartFile == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        long fileSize = multipartFile.getSize();
        if (fileSize > FileConstants.ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过2MB");
        }
        String suffix = FileUtils.getSuffix(multipartFile.getOriginalFilename());
        if (!FileConstants.ALLOW_FORMAT_LIST.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }

    /**
     * 处理文件来源
     *
     * @param inputSource
     * @param file
     */
    @Override
    protected void processFile(Object inputSource, File file) throws Exception {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        multipartFile.transferTo(file);
    }
}
