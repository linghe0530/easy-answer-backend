package com.crane.answer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crane.answer.model.dto.user.UserLoginRequest;
import com.crane.answer.model.dto.user.UserQueryRequest;
import com.crane.answer.model.dto.user.UserRegisterRequest;
import com.crane.answer.model.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crane.answer.model.vo.UserResp;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
* @author crane
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-09-11 16:05:26
*/
public interface UserService extends IService<User> {

    UserResp getUserResp(User user);

    UserResp userLogin(UserLoginRequest request, HttpServletRequest servletRequest);

    Long userRegister(UserRegisterRequest request);

    boolean isAdmin(UserResp loginUser);

    UserResp getLoginInfo();

    void deleteUser(Long id);

    Page<User> listUserAnswerPage(UserQueryRequest request);

    LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    String handleOauthLogin(AuthResponse<?> login);


    boolean addUserSignIn(@Param("userId") long userId);

    List<Integer> getUserSignInRecord(long userId, Integer year);
}
