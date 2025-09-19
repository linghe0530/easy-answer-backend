package com.crane.answer.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.po.InterviewBank;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 题库视图
 *
 * @author crane
 * @date 2025.09.18 上午10:00
 * @description
 */
@Data
public class InterviewBankResp implements Serializable {

    @Serial
    private static final long serialVersionUID = -5541205084167472341L;
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片
     */
    private String picture;

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
     * 题库里的题目列表（分页）
     */
    Page<InterviewResp> interviewPage;

    /**
     * 封装类转对象
     *
     * @param questionBankVO
     * @return
     */
    public static InterviewBank voToObj(InterviewBankResp questionBankVO) {
        if (questionBankVO == null) {
            return null;
        }
        InterviewBank questionBank = new InterviewBank();
        BeanUtils.copyProperties(questionBankVO, questionBank);
        return questionBank;
    }

    /**
     * 对象转封装类
     *
     * @param questionBank
     * @return
     */
    public static InterviewBankResp objToVo(InterviewBank questionBank) {
        if (questionBank == null) {
            return null;
        }
        InterviewBankResp questionBankVO = new InterviewBankResp();
        BeanUtils.copyProperties(questionBank, questionBankVO);
        return questionBankVO;
    }
}
