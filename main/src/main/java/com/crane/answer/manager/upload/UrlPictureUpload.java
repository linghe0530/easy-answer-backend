package com.crane.answer.manager.upload;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.crane.answer.constants.FileConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.utils.FileUtils;
import com.crane.answer.utils.StringUtils;
import com.crane.answer.utils.ThrowUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author crane
 * @date 2025.09.03 下午4:49
 * @description
 **/
@Service
public class UrlPictureUpload extends PictureUploadTemplate {
    public static void main(String[] args) {
        System.out.println(FileUtils.mainName("https://cloudcache.tencent-cloud.com/qcloud/portal/kit/images/presale.a4955999.jpeg"));
    }

    @Override
    protected String getOriginalFilename(Object inputSource) {
        String fileUrl = (String) inputSource;
        return FileUtils.mainName(fileUrl);
    }

    @Override
    protected void validPicture(Object inputSource) {
        String fileUrl = (String) inputSource;
        //校验非空
        ThrowUtils.throwIf(StringUtils.isBlank(fileUrl), ErrorCode.PARAMS_ERROR, "文件地址为空");
        //校验url的格式
        try {
            new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址格式不正确");
        }
        //校验url的协议
        ThrowUtils.throwIf(!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://"),
                ErrorCode.PARAMS_ERROR, "仅支持HTTP和HTTPS协议的文件地址");
        //发送head请求验证文件是否存在

        try (
                HttpResponse response = HttpUtil.createRequest(Method.HEAD, fileUrl)
                        .execute()
        ) {
            if (response.getStatus() != HttpStatus.HTTP_OK) {
                return;
            }
            //文件存在,文件类型校验
            String contentType = response.header("Content-Type");
            if (StringUtils.isNotBlank(contentType)) {
                // 允许的图片类型
                ThrowUtils.throwIf(!FileConstants.ALLOW_CONTENT_TYPES.contains(contentType.toLowerCase()),
                        ErrorCode.PARAMS_ERROR, "文件类型错误");
            }
            //文件大小校验
            String contentLengthStr = response.header("Content-Length");
            if (StringUtils.isNotBlank(contentLengthStr)) {
                try {
                    long contentLength = Long.parseLong(contentLengthStr);
                    ThrowUtils.throwIf(contentLength > 2 * FileConstants.ONE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过2MB");
                } catch (NumberFormatException e) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小格式异常");
                }
            }
        }
    }

    /**
     * 处理文件来源
     *
     * @param inputSource
     * @param file
     */
    @Override
    protected void processFile(Object inputSource, File file) {
        String fileUrl = (String) inputSource;
        HttpUtil.downloadFile(fileUrl, file);
    }
}
