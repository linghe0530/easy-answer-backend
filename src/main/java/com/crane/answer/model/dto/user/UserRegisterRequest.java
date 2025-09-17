package com.crane.answer.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author crane
 * @date 2025.09.01 下午11:34
 * @description 用户注册请求
 **/
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -8491992336279332519L;

    @NotBlank
    @Length(min = 4, max = 64)
    private String userAccount;
    @NotBlank
    @Length(min = 8, max = 64)
    private String userPassword;
    @NotBlank
    @Length(min = 8, max = 64)
    private String checkPassword;

}
