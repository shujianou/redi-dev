package com.wwdj.manager.security.service;

import com.wwdj.manager.security.token.UserToken;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vim
 * @since 2019-02-14
 */
public interface AppUserService extends IService<AppUserEntity> {

    /**
     * 获取用户TOKEN信息
     */
    public UserToken getUserToken(String username,String password);

    /**
     * 根据用户ID获取用户TOKEN
     */
    public UserToken getByUserId(String userId);
}
