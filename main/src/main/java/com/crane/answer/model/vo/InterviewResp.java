package com.crane.answer.model.vo;

import cn.hutool.json.JSONUtil;
import com.crane.answer.model.po.Interview;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 6085566385010437893L;
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 推荐答案
     */
    private String answer;

    /**
     * 创建用户 id
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
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建用户信息
     */
    private UserResp user;

    /**
     * 封装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Interview voToObj(InterviewResp questionVO) {
        if (questionVO == null) {
            return null;
        }
        Interview question = new Interview();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTagList();
        question.setTags(JSONUtil.toJsonStr(tagList));
        return question;
    }

    /**
     * 对象转封装类
     *
     * @param question
     * @return
     */
    public static InterviewResp objToVo(Interview question) {
        if (question == null) {
            return null;
        }
        InterviewResp questionVO = new InterviewResp();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTagList(JSONUtil.toList(JSONUtil.parseArray(question.getTags()), String.class));
        return questionVO;
    }
}
