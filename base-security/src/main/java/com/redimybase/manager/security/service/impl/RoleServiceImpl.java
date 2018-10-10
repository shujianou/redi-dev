package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.RoleEntity;
import com.redimybase.manager.security.mapper.RoleMapper;
import com.redimybase.manager.security.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Override
    public List<String> getRoleNameList(String userId) {
        return baseMapper.getRoleNameList(userId);
    }
}
