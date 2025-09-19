package com.crane.answer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.crane.answer.common.R;
import com.crane.answer.model.dto.user.UserLoginRequest;
import com.crane.answer.model.dto.user.UserRegisterRequest;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.UserService;
import com.crane.answer.utils.DeviceUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author crane
 * @date 2025.09.10 下午9:29
 * @description
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R<UserResp> userLogin(@RequestBody UserLoginRequest request, HttpServletRequest servletRequest) {
        UserResp resp = userService.userLogin(request, servletRequest);
        return R.ok(resp);
    }

    @PostMapping("/register")
    public R<Long> userRegister(@RequestBody UserRegisterRequest request) {
        Long userId = userService.userRegister(request);
        return R.ok(userId);
    }

    @PostMapping("/logout")
    @SaCheckLogin
    public R<Boolean> userLogout(HttpServletRequest request) {
        Object loginId = StpUtil.getLoginId();
        StpUtil.logout(loginId, DeviceUtils.getRequestDevice(request));
        return R.ok(true);
    }

    @GetMapping("/get")
    @SaCheckLogin()
    public R<UserResp> getLoginInfo() {
        UserResp resp = userService.getLoginInfo();
        return R.ok(resp);
    }


    @PostMapping("/add/sign")
    @SaCheckLogin
    public R<Boolean> addSignIn() {
        UserResp resp = userService.getLoginInfo();
        boolean signIn = userService.addUserSignIn(resp.getId());
        return R.ok(signIn);
    }

    @GetMapping("/get/record")
    @SaCheckLogin
    public R<List<Integer>> getSignInRecords(Integer year) {
        UserResp resp = userService.getLoginInfo();
        List<Integer> records = userService.getUserSignInRecord(resp.getId(), year);
        return R.ok(records);
    }

}
