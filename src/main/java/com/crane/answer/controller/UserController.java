package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.crane.answer.common.R;
import com.crane.answer.model.dto.user.UserLoginRequest;
import com.crane.answer.model.dto.user.UserRegisterRequest;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.scoring.ScoringStrategyExecutor;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private ScoringStrategyExecutor strategyExecutor;
    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R<UserResp> userLogin(@RequestBody UserLoginRequest request) {
        UserResp resp = userService.userLogin(request);
        return R.ok(resp);
    }

    @PostMapping("/register")
    public R<String> userRegister(@RequestBody UserRegisterRequest request) {
        Long userId = userService.userRegister(request);
        return R.ok(userId.toString());
    }

    @GetMapping("/get")
    @SaCheckLogin()
    public R<UserResp> getLoginInfo() {
        UserResp resp = userService.getLoginInfo();
        return R.ok(resp);
    }
}
