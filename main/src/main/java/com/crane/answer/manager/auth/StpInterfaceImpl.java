package com.crane.answer.manager.auth;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

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
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        UserResp user = (UserResp) session.get(UserConstants.USER_LOGIN_STATE);
        return List.of(user.getUserRole());
    }
}
