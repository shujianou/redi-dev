package com.wwdj.manager.security.token;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户Token
 * Created by Irany 2018/5/13 12:11
 */
@Data
@ApiModel(value = "用户Token")
public class UserToken implements Serializable {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 用户登录名
     */
    @ApiModelProperty(value = "用户登录名")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效")
    private boolean enabled;

    /**
     * 是否自动注册
     */
    private boolean autoReg;

    public UserToken(String userId, String account, String password, boolean enabled) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.enabled = enabled;
    }

    public UserToken() {
    }
}
