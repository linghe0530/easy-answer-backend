package com.crane.answer.api.imagesearch.sub;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.crane.answer.api.imagesearch.model.ImageUploadResult;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author crane
 * @date 2025.09.06 下午2:08
 * @description 获取以图搜图页面地址（step 1）
 **/

@Slf4j
public class GetImagePageUrlApi {

    public static String getImagePageUrl(String imageUrl) {
        /*

         */

        Map<String, Object> formData = new HashMap<>();
        formData.put("image", imageUrl);
        formData.put("tn", "pc");
        formData.put("from", "pc");
        formData.put("image_source", "PC_UPLOAD_URL");
        // 获取当前时间戳
        long uptime = System.currentTimeMillis();
        // 请求地址
        String fetchUrl = "https://graph.baidu.com/upload?uptime=" + uptime;
        try (HttpResponse response = HttpRequest.post(fetchUrl)
                .header("acs-token", RandomUtil.randomString(1))
                .form(formData)
                .timeout(5000)
                .execute()
        ) {
            if (response.getStatus() != HttpStatus.HTTP_OK) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            String body = response.body();
            ImageUploadResult result = JsonUtils.parse(body, ImageUploadResult.class);
            if (result == null || !Objects.equals(0, result.getStatus())) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
            ImageUploadResult.DataDTO data = result.getData();
            String searchResult = data.getUrl();
            if (StringUtils.isBlank(searchResult)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "未返回有效的结果地址");
            }
            return searchResult;
        } catch (Exception e) {
            log.error("调用百度以图搜图接口失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }
    }


    public static void main(String[] args) {
        String imageUrl = "https://c-ssl.duitang.com/uploads/blog/202207/10/20220710223208_8347f.jpg";
        String result = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果 URL：" + result);

    }
}
