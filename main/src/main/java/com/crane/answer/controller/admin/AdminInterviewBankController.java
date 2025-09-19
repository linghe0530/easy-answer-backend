package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankAddRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankQueryRequest;
import com.crane.answer.model.dto.interviewBank.InterviewBankUpdateRequest;
import com.crane.answer.model.po.InterviewBank;
import com.crane.answer.service.InterviewBankService;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/interview/bank")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminInterviewBankController {

    @Resource
    private InterviewBankService interviewBankService;

    @PostMapping("/list/page")
    public R<Page<InterviewBank>> listInterviewBankByPage(@RequestBody InterviewBankQueryRequest questionBankQueryRequest) {
        long current = questionBankQueryRequest.getCurrent();
        long size = questionBankQueryRequest.getPageSize();
        // 查询数据库
        Page<InterviewBank> questionBankPage = interviewBankService.page(new Page<>(current, size),
                interviewBankService.getQueryWrapper(questionBankQueryRequest));
        return R.ok(questionBankPage);
    }

    @PostMapping("/update")
    public R<Boolean> updateInterviewBank(@RequestBody InterviewBankUpdateRequest request) {
        if (request == null || request.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterviewBank questionBank = new InterviewBank();
        BeanUtils.copyProperties(request, questionBank);
        // 判断是否存在
        long id = request.getId();
        InterviewBank oldQuestionBank = interviewBankService.getById(id);
        ThrowUtils.throwIf(oldQuestionBank == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = interviewBankService.updateById(questionBank);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return R.ok(true);
    }

    @PostMapping("/add")
    public R<Long> addInterviewBank(@RequestBody @Valid InterviewBankAddRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long interviewBankId = interviewBankService.addInterviewBank(request);
        return R.ok(interviewBankId);
    }

    @PostMapping("/delete")
    public R<Boolean> deleteInterviewBank(@RequestBody @Valid DeleteRequest request) {
        if (request == null ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
       interviewBankService.deleteInterviewBank(request.getId());
        return R.ok(true);
    }

}
