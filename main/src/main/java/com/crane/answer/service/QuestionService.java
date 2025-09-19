package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.question.*;
import com.crane.answer.model.po.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.QuestionResp;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
* @author crane
* @description 针对表【question(题目信息表)】的数据库操作Service
* @createDate 2025-09-10 18:40:23
*/
public interface QuestionService extends IService<Question> {

    String addQuestion(QuestionAddRequest questionAddRequest);

    void editQuestion(QuestionEditRequest request);

    Page<QuestionResp> listQuestionRespPage(QuestionQueryRequest request);

    LambdaQueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    QuestionResp getQuestionRespById(Long id);

    void deleteQuestion(DeleteRequest deleteRequest);

    List<QuestionContentDTO> aiGenerateQuestion(AiGenerateQuestionRequest aiGenerateQuestionRequest);

    SseEmitter aiGenerateQuestionStream(AiGenerateQuestionRequest request);
}
