package com.aispread.security.center.model;

import com.aispread.manager.security.entity.RoleEntity;
import com.aispread.manager.security.entity.UserEntity;
import com.aispread.manager.security.token.UserToken;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Vim 2018/11/25 1:17
 *
 * @author Vim
 */
@Data
public class UserDetailInfo implements UserDetails {


    public UserDetailInfo(UserToken userToken) {
        this.account = userToken.getAccount();
        this.password = userToken.getPassword();
        this.status = userToken.getStatus();
        this.roleList = userToken.getRoleList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (RoleEntity role : roleList) {
            authorityList.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !StringUtils.equals(UserEntity.Status.DISABLE, this.status) || !StringUtils.equals(UserEntity.Status.DELETED, this.status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.equals(UserEntity.Status.ENABLE, this.status);
    }


    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 账号状态
     */
    private String status;

    /**
     * 所拥有的角色列表
     */
    private List<RoleEntity> roleList;
}
