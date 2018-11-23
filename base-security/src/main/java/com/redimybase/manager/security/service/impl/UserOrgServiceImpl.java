package com.redimybase.manager.security.service.impl;

import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.mapper.UserOrgMapper;
import com.redimybase.manager.security.service.UserOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户组织关联表 服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-11-23
 */
@Service
public class UserOrgServiceImpl extends ServiceImpl<UserOrgMapper, UserOrgEntity> implements UserOrgService {

}
