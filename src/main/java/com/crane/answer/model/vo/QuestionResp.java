package com.crane.answer.model.vo;

import cn.hutool.core.bean.BeanUtil;
import com.crane.answer.model.dto.question.QuestionContentDTO;
import com.crane.answer.model.po.Question;
import com.crane.answer.utils.JsonUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午6:42
 * @description 题目视图
 **/
@Data
public class QuestionResp implements Serializable {
    @Serial
    private static final long serialVersionUID = 3400866558595237704L;
    /**
     * id
     */
    private Long id;

    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;

    /**
     * 应用 id
     */
    private Long appId;

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
     * 创建用户信息
     */
    private UserResp user;

    /**
     * 封装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionResp questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = BeanUtil.copyProperties(questionVO, Question.class);
        List<QuestionContentDTO> questionContentDTO = questionVO.getQuestionContent();
        question.setQuestionContent(JsonUtils.toJson(questionContentDTO));
        return question;
    }

    /**
     * 对象转封装类
     *
     * @param question
     * @return
     */
    public static QuestionResp objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionResp resp = new QuestionResp();
        BeanUtils.copyProperties(question, resp);
        String questionContent = question.getQuestionContent();
        if (questionContent != null) {
            resp.setQuestionContent(JsonUtils.parseList(questionContent, QuestionContentDTO.class));
        }
        return resp;
    }
}

