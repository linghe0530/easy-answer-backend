package com.crane.answer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.model.po.InterviewQuestion;
import com.crane.answer.service.InterviewQuestionService;
import com.crane.answer.mapper.InterviewQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author crane
* @description 针对表【interview_question(题目)】的数据库操作Service实现
* @createDate 2025-09-18 09:45:28
*/
@Service
public class InterviewQuestionServiceImpl extends ServiceImpl<InterviewQuestionMapper, InterviewQuestion>
    implements InterviewQuestionService{

}




