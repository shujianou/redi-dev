package com.aispread.manager.security.token;

import com.aispread.manager.security.entity.RoleEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    private String status;

    /**
     * 当前用户所拥有的角色集合
     */
    private List<RoleEntity> roleList;

    public UserToken(String userId, String account, String password, boolean enabled) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.enabled = enabled;
    }

    public UserToken(String account, String password) {
        this.account = account;
        this.password = password;
    }



    public UserToken() {
    }
}
