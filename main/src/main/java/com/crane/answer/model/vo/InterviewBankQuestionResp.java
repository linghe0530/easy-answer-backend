package com.crane.answer.model.vo;

import com.crane.answer.model.po.InterviewBankQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题库题目关联视图
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewBankQuestionResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -6539516277727019076L;
    /**
     * id
     */
    private Long id;

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id
     */
    private Long questionId;

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
     * @param questionBankQuestionVO
     * @return
     */
    public static InterviewBankQuestion voToObj(InterviewBankQuestionResp questionBankQuestionVO) {
        if (questionBankQuestionVO == null) {
            return null;
        }
        InterviewBankQuestion questionBankQuestion = new InterviewBankQuestion();
        BeanUtils.copyProperties(questionBankQuestionVO, questionBankQuestion);
        return questionBankQuestion;
    }

    /**
     * 对象转封装类
     *
     * @param questionBankQuestion
     * @return
     */
    public static InterviewBankQuestionResp objToVo(InterviewBankQuestionResp questionBankQuestion) {
        if (questionBankQuestion == null) {
            return null;
        }
        InterviewBankQuestionResp questionBankQuestionVO = new InterviewBankQuestionResp();
        BeanUtils.copyProperties(questionBankQuestion, questionBankQuestionVO);
        return questionBankQuestionVO;
    }
}
