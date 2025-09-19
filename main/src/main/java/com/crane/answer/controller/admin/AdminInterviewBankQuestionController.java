package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.interviewBankQuestion.*;
import com.crane.answer.model.po.InterviewBankQuestion;
import com.crane.answer.service.InterviewBankQuestionService;
import com.crane.answer.service.InterviewBankService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crane
 * @date 2025.09.18 上午10:02
 * @description
 **/
@RestController
@RequestMapping("/bank/question")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminInterviewBankQuestionController {

    @Resource
    private InterviewBankService interviewBankService;
    @Resource
    private InterviewBankQuestionService interviewBankQuestionService;

    @PostMapping("/add")
    public R<Long> addQuestionBankQuestion(@RequestBody InterviewBankQuestionAddRequest request) {
//        ThrowUtils.throwIf(questionBankQuestionAddRequest == null, ErrorCode.PARAMS_ERROR);
//        // todo 在此处将实体类和 DTO 进行转换
//        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
//        BeanUtils.copyProperties(questionBankQuestionAddRequest, questionBankQuestion);
//        // 数据校验
//        questionBankQuestionService.validQuestionBankQuestion(questionBankQuestion, true);
//        // todo 填充默认值
//        User loginUser = userService.getLoginUser(request);
//        questionBankQuestion.setUserId(loginUser.getId());
//        // 写入数据库
//        boolean result = questionBankQuestionService.save(questionBankQuestion);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
//        // 返回新写入的数据 id
//        long newQuestionBankQuestionId = questionBankQuestion.getId();
        return R.ok();
    }

    @PostMapping("/delete")
    public R<Boolean> deleteQuestionBankQuestion(@RequestBody DeleteRequest deleteRequest) {
//        if (deleteRequest == null || deleteRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
//        // 判断是否存在
//        QuestionBankQuestion oldQuestionBankQuestion = questionBankQuestionService.getById(id);
//        ThrowUtils.throwIf(oldQuestionBankQuestion == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可删除
//        if (!oldQuestionBankQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        // 操作数据库
//        boolean result = questionBankQuestionService.removeById(id);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }

    @PostMapping("/update")
    public R<Boolean> updateQuestionBankQuestion(@RequestBody InterviewBankQuestionQueryRequest request) {
//        if (questionBankQuestionUpdateRequest == null || questionBankQuestionUpdateRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // todo 在此处将实体类和 DTO 进行转换
//        QuestionBankQuestion questionBankQuestion = new QuestionBankQuestion();
//        BeanUtils.copyProperties(questionBankQuestionUpdateRequest, questionBankQuestion);
//        // 数据校验
//        questionBankQuestionService.validQuestionBankQuestion(questionBankQuestion, false);
//        // 判断是否存在
//        long id = questionBankQuestionUpdateRequest.getId();
//        QuestionBankQuestion oldQuestionBankQuestion = questionBankQuestionService.getById(id);
//        ThrowUtils.throwIf(oldQuestionBankQuestion == null, ErrorCode.NOT_FOUND_ERROR);
//        // 操作数据库
//        boolean result = questionBankQuestionService.updateById(questionBankQuestion);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }

    @PostMapping("/list/page")
    public R<Page<InterviewBankQuestion>> listQuestionBankQuestionByPage(@RequestBody InterviewBankQuestionQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 查询数据库
        Page<InterviewBankQuestion> page = interviewBankQuestionService.page(new Page<>(current, size),
                interviewBankQuestionService.getQueryWrapper(request));
        return R.ok(page);
    }

    @PostMapping("/remove")
    public R<Boolean> removeQuestionBankQuestion(@RequestBody InterviewBankQuestionRemoveRequest request) {
//        // 参数校验
//        ThrowUtils.throwIf(questionBankQuestionRemoveRequest == null, ErrorCode.PARAMS_ERROR);
//        Long questionBankId = questionBankQuestionRemoveRequest.getQuestionBankId();
//        Long questionId = questionBankQuestionRemoveRequest.getQuestionId();
//        ThrowUtils.throwIf(questionBankId == null || questionId == null, ErrorCode.PARAMS_ERROR);
//        // 构造查询
//        LambdaQueryWrapper<QuestionBankQuestion> lambdaQueryWrapper = Wrappers.lambdaQuery(QuestionBankQuestion.class)
//                .eq(QuestionBankQuestion::getQuestionBankId, questionBankId)
//                .eq(QuestionBankQuestion::getQuestionId, questionId);
//        boolean result = questionBankQuestionService.remove(lambdaQueryWrapper);
        return R.ok();
    }


    @PostMapping("/add/batch")
    public R<Boolean> batchAddQuestionsToBank(@RequestBody InterviewBankQuestionBatchAddRequest request) {
//        ThrowUtils.throwIf(questionBankQuestionBatchAddRequest == null, ErrorCode.PARAMS_ERROR);
//        User loginUser = userService.getLoginUser(request);
//        Long questionBankId = questionBankQuestionBatchAddRequest.getQuestionBankId();
//        List<Long> questionIdList = questionBankQuestionBatchAddRequest.getQuestionIdList();
//        questionBankQuestionService.batchAddQuestionsToBank(questionIdList, questionBankId, loginUser);
        return R.ok(true);
    }


    @PostMapping("/remove/batch")
    public R<Boolean> batchRemoveQuestionsFromBank(@RequestBody InterviewBankQuestionBatchRemoveRequest request) {
//        ThrowUtils.throwIf(questionBankQuestionBatchRemoveRequest == null, ErrorCode.PARAMS_ERROR);
//        Long questionBankId = questionBankQuestionBatchRemoveRequest.getQuestionBankId();
//        List<Long> questionIdList = questionBankQuestionBatchRemoveRequest.getQuestionIdList();
//        questionBankQuestionService.batchRemoveQuestionsFromBank(questionIdList, questionBankId);
        return R.ok(true);
    }
}
