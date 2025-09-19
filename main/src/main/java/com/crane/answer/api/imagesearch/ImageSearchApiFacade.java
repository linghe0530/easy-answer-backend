package com.crane.answer.api.imagesearch;

import com.crane.answer.api.imagesearch.model.BaiduSimilarImageResponse;
import com.crane.answer.api.imagesearch.sub.GetImageFirstUrlApi;
import com.crane.answer.api.imagesearch.sub.GetImageListApi;
import com.crane.answer.api.imagesearch.sub.GetImagePageUrlApi;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.06 下午3:28
 * @description
 **/
public class ImageSearchApiFacade {
    public static List<BaiduSimilarImageResponse.ResponseData.ImageItem> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        return GetImageListApi.getImageList(imageFirstUrl);


    }

    public static void main(String[] args) {
        List<BaiduSimilarImageResponse.ResponseData.ImageItem> imageItems = searchImage("https://c-ssl.duitang.com/uploads/blog/202302/06/20230206113723_52f25.jpg");
        System.out.println(imageItems);
    }
}
