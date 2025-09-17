package com.crane.answer.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.01 下午11:34
 * @description 用户登录请求
 **/
@Data
public class UserLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -8491992336279332519L;

    @NotBlank(message = "用户账号不能为空")
    @Length(min = 4, max = 64, message = "用户账号错误")
    private String userAccount;
    @NotBlank(message = "用户账号不能为空")
    @Length(min = 4, max = 64, message = "用户密码错误")
    private String userPassword;

}
