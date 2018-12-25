package com.aispread.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aispread.manager.security.entity.UserRoleEntity;
import com.aispread.manager.security.mapper.UserRoleMapper;
import com.aispread.manager.security.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class UserRoleServiceImpl<ID> extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {

    @Override
    public List<ID> listID(Wrapper<UserRoleEntity> queryWrapper) {
        return (List<ID>) super.listObjs(queryWrapper);
    }
}
