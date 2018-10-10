package com.redimybase.security.shiro.token;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户Token
 * Created by Irany 2018/5/13 12:11
 */
@Data
public class UserToken implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户登录名
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否有效
     */
    private boolean enabled;

    public UserToken(String userId, String account, String password, boolean enabled) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.enabled = enabled;
    }

    public UserToken() {
    }
}
