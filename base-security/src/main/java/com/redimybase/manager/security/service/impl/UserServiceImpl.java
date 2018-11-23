package com.redimybase.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.security.entity.OrgEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.entity.UserOrgEntity;
import com.redimybase.manager.security.mapper.UserMapper;
import com.redimybase.manager.security.service.OrgService;
import com.redimybase.manager.security.service.UserOrgService;
import com.redimybase.manager.security.service.UserService;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
