package com.crane.answer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author crane
 * @date 2025.09.17 上午9:57
 * @description
 **/
@Configuration
@ConfigurationProperties(prefix = "oauth.gitee")
@Data
public class OauthGiteeConfig {

    private String clientId;
    private String clientSecret;
    private String redirectUrl;

}
