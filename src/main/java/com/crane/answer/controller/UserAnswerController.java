package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerAddRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerEditRequest;
import com.crane.answer.model.dto.userAnswer.UserAnswerQueryRequest;
import com.crane.answer.model.vo.UserAnswerResp;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/answer")
@SaCheckLogin
public class UserAnswerController {
    @Resource
    private UserService userService;
    @Resource
    private UserAnswerService userAnswerService;

    @GetMapping("/generate/id")
    public R<Long> generateUserAnswerId() {
        return R.ok(IdUtil.getSnowflakeNextId());
    }

    @PostMapping("/add")
    public R<String> addUserAnswer(@RequestBody @Valid UserAnswerAddRequest request) {
        Long answerId = userAnswerService.addUserAnswer(request);
        return R.ok(answerId.toString());
    }

    @PostMapping("/delete")
    public R<Boolean> deleteUserAnswer(@RequestBody DeleteRequest request) {
        return R.ok();
    }

    @GetMapping("/resp")
    public R<UserAnswerResp> getUserAnswerRespById(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserAnswerResp resp = userAnswerService.getUserAnswerRespById(id);
        return R.ok(resp);
    }

    @PostMapping("/list/page/resp")
    public R<Page<UserAnswerResp>> listUserAnswerRespByPage(@RequestBody UserAnswerQueryRequest request) {
        int pageSize = request.getPageSize();
        if (pageSize > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<UserAnswerResp> pageResp = userAnswerService.getUserAnswerRespPage(request);
        return R.ok(pageResp);
    }

    @PostMapping("/my/list/page/resp")
    public R<Page<UserAnswerResp>> listMyUserAnswerRespByPage(@RequestBody UserAnswerQueryRequest request) {
        int pageSize = request.getPageSize();
        if (pageSize > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserResp loginInfo = userService.getLoginInfo();
        request.setUserId(loginInfo.getId());
        Page<UserAnswerResp> pageResp = userAnswerService.getUserAnswerRespPage(request);
        return R.ok(pageResp);
    }

    @PostMapping("/edit")
    public R<Boolean> editUserAnswer(@RequestBody UserAnswerEditRequest request) {
        return R.ok();
    }
}
