package com.crane.answer.model.vo;

import com.crane.answer.model.po.Picture;
import com.crane.answer.utils.JsonUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.02 下午5:09
 * @description
 **/
@Data
public class PictureResp implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 图片 url
     */
    private String url;

    /**
     * 缩略图 url
     */
    private String thumbnailUrl;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 分类
     */
    private String category;

    /**
     * 文件体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 图片主色调
     */
    private String picColor;

    /**
     * 用户 id
     */
    private Long userId;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserResp user;

    /**
     * 权限列表
     */
    private List<String> permissionList = new ArrayList<>();

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 封装类转对象
     */
    public static Picture voToObj(PictureResp response) {
        if (response == null) {
            return null;
        }
        Picture picture = new Picture();
        BeanUtils.copyProperties(response, picture);
        // 类型不同，需要转换
        picture.setTags(JsonUtils.toJson(response.getTags()));
        return picture;
    }

    /**
     * 对象转封装类
     */
    public static PictureResp objToVo(Picture picture) {
        if (picture == null) {
            return null;
        }
        PictureResp resp = new PictureResp();
        BeanUtils.copyProperties(picture, resp);
        // 类型不同，需要转换
        resp.setTags(JsonUtils.parseList(picture.getTags()));
        return resp;
    }
}