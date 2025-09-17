package com.crane.answer.api.imagesearch.sub;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.crane.answer.api.imagesearch.model.BaiduSimilarImageResponse;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.utils.CollUtils;
import com.crane.answer.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.06 下午2:56
 * @description 获取图片列表 （step 3）
 **/
@Slf4j
public class GetImageListApi {

    /**
     * 获取图片列表
     *
     * @param url
     * @return
     */

    public static List<BaiduSimilarImageResponse.ResponseData.ImageItem> getImageList(String url) {
        try (HttpResponse response = HttpUtil.createGet(url).execute()) {

            // 获取响应内容
            int statusCode = response.getStatus();
            String body = response.body();
            // 处理响应
            if (statusCode == 200) {
                // 解析 JSON 数据并处理
                return processResponse(body);
            } else {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
            }
        } catch (Exception e) {
            log.error("获取图片列表失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取图片列表失败");
        }
    }

    /**
     * 处理接口响应内容
     *
     * @param responseBody 接口返回的JSON字符串
     */
    private static List<BaiduSimilarImageResponse.ResponseData.ImageItem> processResponse(String responseBody) {
        // 解析响应对象
        BaiduSimilarImageResponse result = JsonUtils.parse(responseBody, BaiduSimilarImageResponse.class);
        if (result == null || !result.getStatus().equals(0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未获取到图片列表");
        }
        BaiduSimilarImageResponse.ResponseData data = result.getData();
        if (data == null || CollUtils.isEmpty(data.getList())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未获取到图片列表");
        }
        return result.getData().getList();
    }

    public static void main(String[] args) {
        String url = "https://graph.baidu.com/ajax/pcsimi?carousel=503&entrance=GENERAL&extUiData%5BisLogoShow%5D=1&inspire=general_pc&limit=30&next=2&render_type=card&session_id=5554272990786666046&sign=121a553f748b9977d344b01757142110&tk=b861d&tpl_from=pc";
        List<BaiduSimilarImageResponse.ResponseData.ImageItem> imageList = getImageList(url);
        System.out.println("搜索成功" + imageList);
    }
}
