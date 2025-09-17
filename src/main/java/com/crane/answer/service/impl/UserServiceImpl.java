package com.crane.answer.service.impl;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crane.answer.constants.UserConstants;
import com.crane.answer.exception.BusinessException;
import com.crane.answer.exception.ErrorCode;
import com.crane.answer.model.dto.oauth.GiteeUserInfo;
import com.crane.answer.model.dto.user.UserLoginRequest;
import com.crane.answer.model.dto.user.UserQueryRequest;
import com.crane.answer.model.dto.user.UserRegisterRequest;
import com.crane.answer.model.enums.UserRoleEnum;
import com.crane.answer.model.po.User;
import com.crane.answer.model.vo.UserResp;
import com.crane.answer.service.UserService;
import com.crane.answer.mapper.UserMapper;
import com.crane.answer.utils.DigestUtil;
import com.crane.answer.utils.JsonUtils;
import com.crane.answer.utils.StringUtils;
import com.crane.answer.utils.ThrowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author crane
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-09-11 16:05:26
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private SaTokenConfig saTokenConfig;

    @Override
    public UserResp getUserResp(User user) {
        if (user == null) {
            return null;
        }
        return BeanUtil.copyProperties(user, UserResp.class);
    }

    @Override
    public UserResp userLogin(UserLoginRequest request) {
        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String encryptPassword = DigestUtil.getEncryptPassword(userPassword);
        User user = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword)
                .one();
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "账号或密码错误");
        UserResp resp = BeanUtil.copyProperties(user, UserResp.class);
        StpUtil.login(user.getId());
        StpUtil.getSession().set(UserConstants.USER_LOGIN_STATE, resp);
        return resp;
    }

    @Override
    public Long userRegister(UserRegisterRequest request) {
        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();

        //1.校验参数
        if (!Objects.equals(userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        //2.检查用户账号是否和数据库中已有的重复
        Long count = this.lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .count();
        if (count > 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        //3.密码加密
        String encryptPassword = DigestUtil.getEncryptPassword(userPassword);
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        //4.插入数据库
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYS_ERROR, "注册失败,请重试");
        }
        return user.getId();
    }

    @Override
    public boolean isAdmin(UserResp user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public UserResp getLoginInfo() {
        StpUtil.renewTimeout(saTokenConfig.getTimeout());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        log.info("{}", tokenInfo);
        return (UserResp) StpUtil.getSession().get(UserConstants.USER_LOGIN_STATE);
    }

    @Override
    public void deleteUser(Long id) {
        boolean isSuccess = this.removeById(id);
        if (!isSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        //todo 删除相关表
    }

    @Override
    public Page<User> listUserAnswerPage(UserQueryRequest request) {
        return this.page(new Page<>(request.getCurrent(), request.getPageSize()), getQueryWrapper(request));
    }

    @Override
    public LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (userQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();

        // 使用LambdaQueryWrapper实现类型安全查询
        queryWrapper.eq(id != null, User::getId, id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), User::getUserRole, userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), User::getUserProfile, userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), User::getUserName, userName);
        return queryWrapper;
    }

    @Override
    public String handleOauthLogin(AuthResponse<?> authResponse) {
        if (authResponse.getCode() != 2000) {
            return authResponse.getMsg();
        }
        GiteeUserInfo giteeUserInfo = JsonUtils.parse(JsonUtils.toJson(authResponse.getData()), GiteeUserInfo.class);
        GiteeUserInfo.Token token = giteeUserInfo.getToken();
        String userAccount = DigestUtils.md5Hex(token.getAccessToken());
        String id = giteeUserInfo.getUuid();
        User user = this.lambdaQuery()
                .eq(User::getId, id)
                .one();

        String password = DigestUtil.getEncryptPassword(token.getAccessToken());
        if (user == null) {
            user = new User();
            user.setId(Long.parseLong(id));
            user.setUserAccount(userAccount);
            user.setUserPassword(password);
            user.setUserName(giteeUserInfo.getUsername());
            user.setUserAvatar(giteeUserInfo.getAvatar());
            user.setUserProfile(giteeUserInfo.getRawUserInfo().getBio());
            user.setCreateTime(new Date());
            user.setUserRole(UserRoleEnum.USER.getValue());
            this.save(user);
        }
        StpUtil.login(user.getId());
        StpUtil.getSession().set(UserConstants.USER_LOGIN_STATE, this.getUserResp(user));
        return "";
    }

}




