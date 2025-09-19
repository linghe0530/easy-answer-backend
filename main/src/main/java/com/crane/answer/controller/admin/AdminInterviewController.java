package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.interview.InterviewAddRequest;
import com.crane.answer.model.dto.interview.InterviewBatchDeleteRequest;
import com.crane.answer.model.dto.interview.InterviewQueryRequest;
import com.crane.answer.model.dto.interview.InterviewUpdateRequest;
import com.crane.answer.model.po.Interview;
import com.crane.answer.service.InterviewService;
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
@RequestMapping("/v3/interview")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminInterviewController {
    @Resource
    private InterviewService interviewService;

    @PostMapping("/add")
    public R<Long> addQuestion(@RequestBody InterviewAddRequest request) {
//        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
//        // todo 在此处将实体类和 DTO 进行转换
//        Question question = new Question();
//        BeanUtils.copyProperties(questionAddRequest, question);
//        List<String> tags = questionAddRequest.getTags();
//        if (tags != null) {
//            question.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 数据校验
//        questionService.validQuestion(question, true);
//        // todo 填充默认值
//        User loginUser = userService.getLoginUser(request);
//        question.setUserId(loginUser.getId());
//        // 写入数据库
//        boolean result = questionService.save(question);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
//        // 返回新写入的数据 id
//        long newQuestionId = question.getId();
        return R.ok();
    }


    @PostMapping("/delete")
    public R<Boolean> deleteQuestion(@RequestBody DeleteRequest request) {
//        if (deleteRequest == null || deleteRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
//        // 判断是否存在
//        Question oldQuestion = questionService.getById(id);
//        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可删除
//        if (!oldQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        // 操作数据库
//        boolean result = questionService.removeById(id);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }

    @PostMapping("/update")
    public R<Boolean> updateQuestion(@RequestBody InterviewUpdateRequest request) {
//        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // todo 在此处将实体类和 DTO 进行转换
//        Question question = new Question();
//        BeanUtils.copyProperties(questionUpdateRequest, question);
//        List<String> tags = questionUpdateRequest.getTags();
//        if (tags != null) {
//            question.setTags(JSONUtil.toJsonStr(tags));
//        }
//        // 数据校验
//        questionService.validQuestion(question, false);
//        // 判断是否存在
//        long id = questionUpdateRequest.getId();
//        Question oldQuestion = questionService.getById(id);
//        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
//        // 操作数据库
//        boolean result = questionService.updateById(question);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }

    @PostMapping("/list/page")
    public R<Page<Interview>> listQuestionByPage(@RequestBody InterviewQueryRequest request) {
//        ThrowUtils.throwIf(questionQueryRequest == null, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        Page<Question> questionPage = questionService.listQuestionByPage(questionQueryRequest);
        return R.ok();
    }

    @PostMapping("/delete/batch")
    public R<Boolean> batchDeleteQuestions(@RequestBody InterviewBatchDeleteRequest request) {
//        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
//        questionService.batchDeleteQuestions(questionBatchDeleteRequest.getQuestionIdList());
        return R.ok(true);
    }
}
