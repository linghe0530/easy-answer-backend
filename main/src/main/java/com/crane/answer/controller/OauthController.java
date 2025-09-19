package com.crane.answer.controller;

import com.crane.answer.config.OauthGiteeConfig;
import com.crane.answer.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author crane
 * @date 2025.09.17 上午9:53
 * @description
 **/
@RestController
@RequestMapping("/oauth")
public class OauthController {
    @Resource
    private OauthGiteeConfig oauthGiteeConfig;
    @Resource
    private UserService userService;
    @Value("${web.path}")
    private String webPath;

    @RequestMapping("/gitee/login")
    public void giteeLogin(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getGiteeAuthRequest();
        String url = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(url);
    }

    @RequestMapping("/gitee/login/callback")
    public void giteeLoginCallback(HttpServletResponse response, AuthCallback callback) throws IOException {
        AuthRequest authRequest = getGiteeAuthRequest();
        String msg = userService.handleOauthLogin(authRequest.login(callback));
        response.sendRedirect(webPath);
    }

    public AuthRequest getGiteeAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId(oauthGiteeConfig.getClientId())
                .clientSecret(oauthGiteeConfig.getClientSecret())
                .redirectUri(oauthGiteeConfig.getRedirectUrl())
                .build());
    }

}
