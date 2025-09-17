package com.crane.answer.controller.admin;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.common.R;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.dto.DeleteRequest;
import com.crane.answer.model.dto.user.UserQueryRequest;
import com.crane.answer.model.dto.user.UserUpdateRequest;
import com.crane.answer.model.po.User;
import com.crane.answer.service.UserAnswerService;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author crane
 * @date 2025.09.14 下午5:15
 * @description
 **/
@RestController
@RequestMapping("/v3/user")
@SaCheckRole(value = {UserConstants.ADMIN_ROLE})
public class AdminUserController {

    @Resource
    private UserAnswerService userAnswerService;
    @Resource
    private UserService userService;


    @PostMapping("/list/page")
    public R<Page<User>> listUserPage(@RequestBody UserQueryRequest request) {
        Page<User> pageResp = userService.listUserAnswerPage(request);
        return R.ok(pageResp);
    }

    @PostMapping("/update")
    public R<Boolean> updateUserAnswer(@RequestBody UserUpdateRequest request) {
        return R.ok();
    }

    @PostMapping("/delete")
    public R<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {

        userService.deleteUser(deleteRequest.getId());
        return R.ok(true);
    }
}
