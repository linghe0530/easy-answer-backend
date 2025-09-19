package com.crane.answer.constants;

import java.util.Arrays;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 上午12:25
 * @description
 **/
public interface FileConstants {


    /**
     * 1m大小
     */
    int ONE_M = 1024 * 1024;
    String WEBP = ".webp";

    List<String> ALLOW_FORMAT_LIST = Arrays.asList(
            // 图片格式
            "jpeg", "png", "jpg", "webp", "gif", "bmp", "svg", "tiff", "ico");
    // 允许的图片类型
    List<String> ALLOW_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/webp");

    //excel类型
    List<String> EXCEL_FILE_SUFFIX_LIST = Arrays.asList("xlsx", "xls");
}
