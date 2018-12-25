package com.redimybase.module.security.api.impl;

import com.aispread.manager.security.entity.RoleEntity;
import com.aispread.manager.security.entity.UserEntity;
import com.aispread.manager.security.entity.UserRoleEntity;
import com.aispread.manager.security.service.UserRoleService;
import com.aispread.manager.security.service.impl.UserRoleServiceImpl;
import com.aispread.service.api.security.UserCheckApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aispread.manager.security.service.ResourceService;
import com.aispread.manager.security.service.RoleService;
import com.aispread.manager.security.service.impl.UserServiceImpl;
import com.aispread.manager.security.token.UserToken;
import com.aispread.manager.security.constant.UserStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irany 2018/5/13 12:18
 */
@RestController
@RequestMapping("api/security/check")
@PropertySource(value = {"classpath:application.properties"}, encoding = "utf-8")
public class UserCheckApiImpl implements UserCheckApi {
    /**
     * 根据用户ID获取用户token信息
     */
    @Override
    @GetMapping(value = "userToken/{userId}")
    public UserToken getByUserId(@PathVariable("userId") String userId) {
        UserEntity user = service.getById(userId);

        if (user != null) {
            if (!StringUtils.equals(user.getStatus(), UserStatus.删除)) {
                return toUserToken(user);
            }
        }
        return null;
    }

    @Override
    @GetMapping("userToken/account/{account}")
    public UserToken getByAccount(@PathVariable("account") String account) {
        return toUserToken(service.getOne(new QueryWrapper<UserEntity>().eq("account", account)));
    }

    private UserToken toUserToken(UserEntity user) {
        if (null == user) {
            return null;
        }
        UserToken userToken = new UserToken();
        List<UserRoleEntity> userRoleList = userRoleService.list(new QueryWrapper<UserRoleEntity>().eq("user_id", user.getId()).select("role_id"));
        List<String> roleIdList = new ArrayList<>();
        for (UserRoleEntity userRole : userRoleList) {
            roleIdList.add(userRole.getRoleId());
        }
        userToken.setRoleList(new ArrayList<>(roleService.listByIds(roleIdList)));
        userToken.setEnabled(StringUtils.equals(user.getStatus(), UserStatus.启用));
        userToken.setAccount(user.getAccount());
        userToken.setPassword(user.getPassword());
        userToken.setUserId(user.getId());
        userToken.setStatus(user.getStatus());
        return userToken;
    }

    /**
     * 获取用户拥有的角色
     */
    @Override
    @GetMapping("roleNameList/{userId}")
    public List<String> getRoleNameList(@PathVariable("userId") String userId) {
        return roleService.getRoleNameList(userId);
    }

    /**
     * 获取用户拥有的资源
     */
    @Override
    @GetMapping("resNameList/{userId}")
    public List<String> getResNameList(@PathVariable("userId") String userId) {
        return resourceService.getResNameList(userId);
    }

    /**
     * 获取用户拥有的资源KEY
     */
    @Override
    @GetMapping(value = "resKeyList/{userId}")
    public List<String> getResKeyList(@PathVariable("userId") String userId) {
        return resourceService.getResKeyList(userId);
    }

    /**
     * 根据账号和密码获取用户Token
     */
    @Override
    @RequestMapping(value = "userToken", method = RequestMethod.POST)
    public UserToken getUserToken(@RequestBody UserToken userToken) {
        UserEntity user = null;
        if (StringUtils.equals(useAccount, "1")) {
            user = service.getOne(new QueryWrapper<UserEntity>().eq("ACCOUNT", userToken.getAccount()));
        }

        if (StringUtils.equals(useEmail, "1") && user == null) {
            user = service.getOne(new QueryWrapper<UserEntity>().eq("EMAIL", userToken.getAccount()));
        }

        if (StringUtils.equals(usePhone, "1") && user == null) {
            user = service.getOne(new QueryWrapper<UserEntity>().eq("PHONE", userToken.getAccount()));
        }

        if (user != null && StringUtils.equals(user.getPassword(), userToken.getPassword())) {
            if (!StringUtils.equals(user.getStatus(), UserStatus.删除)) {
                return new UserToken(user.getId(), user.getAccount(), user.getPassword(), StringUtils.equals(user.getStatus(), UserStatus.启用));
            }
        }
        return null;
    }

    /**
     * 根据用户ID获取用户信息
     */
    @Override
    @GetMapping(value = "get/{userId}")
    public UserEntity getLoginInfo(@PathVariable("userId") String userId) {
        return service.getById(userId);
    }

    @Value("${redi.usePhone}")
    public String usePhone;

    @Value("${redi.useEmail}")
    public String useEmail;

    @Value("${redi.useAccount}")
    public String useAccount;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleServiceImpl<String> userRoleService;


}
