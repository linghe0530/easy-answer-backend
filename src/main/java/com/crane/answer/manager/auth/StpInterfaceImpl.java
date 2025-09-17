package com.crane.answer.manager.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.crane.answer.model.po.User;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author crane
 * @date 2025.09.11 下午7:02
 * @description
 **/
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long id = Long.valueOf((String) loginId);
        User user = userService.getById(id);
        return List.of(user.getUserRole());
    }
}
