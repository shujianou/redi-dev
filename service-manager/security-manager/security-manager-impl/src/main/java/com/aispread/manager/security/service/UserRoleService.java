package com.aispread.manager.security.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aispread.manager.security.entity.UserRoleEntity;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface UserRoleService extends IService<UserRoleEntity> {
    List listID(Wrapper<UserRoleEntity> queryWrapper);
}
