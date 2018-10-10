package com.redimybase.security.shiro.dao;

import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.security.shiro.token.UserToken;

import java.util.List;

/**
 * 用户验证interface
 * Created by Irany 2018/5/13 12:13
 */
public interface UserCheckDao {

    /**
     * 根据用户ID获取用户token信息
     * @param userId
     * @return
     */
    UserToken getByUserId(String userId);

    /**
     * 获取用户拥有的角色
     * @param userId
     * @return
     */
    List<String> getRoleNameList(String userId);


    /**
     * 获取用户拥有的资源
     * @param userId
     * @return
     */
    List<String> getResNameList(String userId);


    /**
     * 获取用户拥有的资源KEY
     * @param userId
     * @return
     */
    List<String> getResKeyList(String userId);

    /**
     * 根据账号和密码获取用户Token
     * @param username
     * @param pwd
     * @return
     */
    UserToken getUserToken(String username,String pwd);

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    UserEntity getLoginInfo(String userId);

}
