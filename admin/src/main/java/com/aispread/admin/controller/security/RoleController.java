package com.aispread.admin.controller.security;

import com.aispread.manager.security.entity.UserEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.security.entity.RoleEntity;
import com.aispread.manager.security.mapper.RoleMapper;
import com.aispread.manager.security.service.impl.RoleServiceImpl;
import com.redimybase.security.shiro.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 角色管理Controller
 * Created by Vim 2018/11/23 16:56
 *
 * @author Vim
 */
@RestController
@RequestMapping("role")
public class RoleController extends TableController<String, RoleEntity, RoleMapper, RoleServiceImpl> {

    @Override
    public void beforeSave(RoleEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //key相同不添加
            if (service.count(new QueryWrapper<RoleEntity>().eq("code", entity.getCode())) > 0) {
                throw new BusinessException(R.失败, "角色code已存在");
            }
            entity.setCreateTime(new Date());

            entity.setCreator(userEntity.getUserName());
            entity.setCreatorId(userEntity.getId());
        } else {
            entity.setReviser(userEntity.getUserName());
            entity.setReviserId(userEntity.getId());
            entity.setUpdateTime(new Date());
        }

        super.beforeSave(entity);
    }

    @Autowired
    private RoleServiceImpl service;

    @Override
    protected RoleServiceImpl getService() {
        return service;
    }
}
