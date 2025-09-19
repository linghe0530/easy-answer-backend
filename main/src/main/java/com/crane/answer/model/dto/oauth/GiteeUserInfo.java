package com.crane.answer.model.dto.oauth;



import lombok.Data;

/**
 * @author crane
 * @date 2025.09.17 上午10:28
 * @description 对应JSON结构的用户完整信息，包含基本信息、令牌信息和原始用户数据
 **/
@Data
public class GiteeUserInfo {

    /**
     * 用户唯一标识
     */
    private String uuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人博客地址
     */
    private String blog;

    /**
     * 所属公司
     */
    private String company;

    /**
     * 所在地
     */
    private String location;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 性别
     */
    private String gender;

    /**
     * 来源渠道
     */
    private String source;

    /**
     * 令牌信息对象
     */
    private Token token;

    /**
     * 原始用户信息（第三方平台返回的原始数据）
     */
    private RawUserInfo rawUserInfo;

    /**
     * 是否为快照用户
     */
    private boolean snapshotUser;

    /**
     * 令牌信息内部类
     * 包含访问令牌、刷新令牌等认证相关信息
     */
    @Data
    public static class Token {

        /**
         * 访问令牌
         */
        private String accessToken;

        /**
         * 访问令牌过期时间（秒）
         */
        private int expireIn;

        /**
         * 刷新令牌
         */
        private String refreshToken;

        /**
         * 刷新令牌过期时间（秒）
         */
        private int refreshTokenExpireIn;

        /**
         * 用户ID
         */
        private String uid;

        /**
         * 开放平台ID
         */
        private String openId;

        /**
         * 访问代码
         */
        private String accessCode;

        /**
         * 联合ID（多平台统一标识）
         */
        private String unionId;

        /**
         * 权限范围
         */
        private String scope;

        /**
         * 令牌类型
         */
        private String tokenType;

        /**
         * ID令牌
         */
        private String idToken;

        /**
         * MAC算法
         */
        private String macAlgorithm;

        /**
         * MAC密钥
         */
        private String macKey;

        /**
         * 授权码
         */
        private String code;

        /**
         * 是否为快照用户
         */
        private boolean snapshotUser;

        /**
         * OAuth令牌
         */
        private String oauthToken;

        /**
         * OAuth令牌密钥
         */
        private String oauthTokenSecret;

        /**
         * 用户ID（第三方平台）
         */
        private String userId;

        /**
         * 屏幕名称
         */
        private String screenName;

        /**
         * OAuth回调确认状态
         */
        private String oauthCallbackConfirmed;
    }

    /**
     * 原始用户信息内部类
     * 包含第三方平台返回的原始用户数据
     */
    @Data
    public static class RawUserInfo {

        /**
         * Gists地址
         */
        private String gists_url;

        /**
         * 仓库地址
         */
        private String repos_url;

        /**
         * 关注列表地址
         */
        private String following_url;

        /**
         * 个人简介
         */
        private String bio;

        /**
         * 创建时间
         */
        private String created_at;

        /**
         * 备注信息
         */
        private String remark;

        /**
         * 登录名
         */
        private String login;

        /**
         * 用户类型
         */
        private String type;

        /**
         * 个人博客地址
         */
        private String blog;

        /**
         * 订阅列表地址
         */
        private String subscriptions_url;

        /**
         * 微博账号
         */
        private String weibo;

        /**
         * 更新时间
         */
        private String updated_at;

        /**
         * 第三方平台用户ID
         */
        private int id;

        /**
         * 公共仓库数量
         */
        private int public_repos;

        /**
         * 电子邮箱
         */
        private String email;

        /**
         * 组织列表地址
         */
        private String organizations_url;

        /**
         * 星标项目地址
         */
        private String starred_url;

        /**
         * 粉丝列表地址
         */
        private String followers_url;

        /**
         * 公共Gists数量
         */
        private int public_gists;

        /**
         * 个人主页API地址
         */
        private String url;

        /**
         * 收到的事件地址
         */
        private String received_events_url;

        /**
         * 已 watching 的项目数量
         */
        private int watched;

        /**
         * 粉丝数量
         */
        private int followers;

        /**
         * 头像URL
         */
        private String avatar_url;

        /**
         * 事件列表地址
         */
        private String events_url;

        /**
         * 个人主页HTML地址
         */
        private String html_url;

        /**
         * 关注的用户数量
         */
        private int following;

        /**
         * 姓名
         */
        private String name;

        /**
         * 已 star 的项目数量
         */
        private int stared;
    }
}
