package com.aispread.service.api.security;

import com.aispread.manager.security.entity.UserEntity;
import com.aispread.service.api.security.fallback.UserCheckApiFallback;
import com.aispread.manager.security.token.UserToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户验证interface
 * Created by Irany 2018/5/13 12:13
 *
 * @author Irany
 */
@FeignClient(name = "security-service", fallback = UserCheckApiFallback.class)
public interface UserCheckApi {

    /**
     * 根据用户ID获取用户token信息
     */
    @GetMapping(value = "/api/security/check/userToken/{userId}")
    public UserToken getByUserId(@PathVariable("userId") String userId);

    /**
     * 根据用户ID获取用户token信息
     */
    @GetMapping(value = "/api/security/check/userToken/account/{account}")
    public UserToken getByAccount(@PathVariable("account") String account);

    /**
     * 获取用户拥有的角色
     */
    @GetMapping("/api/security/check/roleNameList/{userId}")
    public List<String> getRoleNameList(@PathVariable("userId") String userId);


    /**
     * 获取用户拥有的资源
     */
    @GetMapping("/api/security/check/resNameList/{userId}")
    public List<String> getResNameList(@PathVariable("userId") String userId);


    /**
     * 获取用户拥有的资源KEY
     */
    @GetMapping(value = "/api/security/check/resKeyList/{userId}")
    public List<String> getResKeyList(@PathVariable("userId") String userId);

    /**
     * 根据账号和密码获取用户Token
     */
    @RequestMapping(value = "/api/security/check/userToken", method = RequestMethod.POST)
    public UserToken getUserToken(@RequestBody UserToken userToken);

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping(value = "/api/security/check/get/{userId}")
    public UserEntity getLoginInfo(@PathVariable("userId") String userId);

}
