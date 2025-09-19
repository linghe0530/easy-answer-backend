package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.question.*;
import com.crane.answer.model.vo.QuestionResp;
import com.crane.answer.service.QuestionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionService questionService;

    @PostMapping("/add")
    @SaCheckLogin
    public R<String> addQuestion(@RequestBody @Valid QuestionAddRequest questionAddRequest) {
        String questionId = questionService.addQuestion(questionAddRequest);
        return R.ok(questionId);
    }

    @PostMapping("/edit")
    @SaCheckLogin
    public R<Boolean> editQuestion(@RequestBody QuestionEditRequest request) {
        questionService.editQuestion(request);
        return R.ok(true);
    }

    @PostMapping("/delete")
    @SaCheckLogin
    public R<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        questionService.deleteQuestion(deleteRequest);
        return R.ok();
    }

    @GetMapping("/get/resp")
    public R<QuestionResp> getQuestionRespById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionResp resp = questionService.getQuestionRespById(id);
        return R.ok(resp);
    }

    @PostMapping("/list/page/resp")
    public R<Page<QuestionResp>> listQuestionRespPage(@RequestBody QuestionQueryRequest request) {
        Page<QuestionResp> pageResp = questionService.listQuestionRespPage(request);
        return R.ok(pageResp);
    }


    @PostMapping("/ai_generate")
    @SaCheckLogin
    public R<List<QuestionContentDTO>> aiGenerateQuestion(
            @RequestBody @Valid AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        List<QuestionContentDTO> list = questionService.aiGenerateQuestion(aiGenerateQuestionRequest);
        return R.ok(list);
    }

    @GetMapping("/ai_generate/sse")
    public SseEmitter aiGenerateQuestionSse(@ParameterObject @Valid AiGenerateQuestionRequest aiGenerateQuestionRequest) {
        return questionService.aiGenerateQuestionStream(aiGenerateQuestionRequest);
    }
}
