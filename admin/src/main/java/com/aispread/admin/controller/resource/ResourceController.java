package com.aispread.admin.controller.resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.ResourceEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.ResourceMapper;
import com.redimybase.manager.security.service.impl.ResourceServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 资源管理
 * Created by Irany 2018/6/15 23:21
 */
@RestController
@RequestMapping(value = "resource")
public class ResourceController extends TableController<String, ResourceEntity, ResourceMapper, ResourceServiceImpl> {


    @PostMapping(value = "menuNodeList")
    @RequiresPermissions(value = {"system_resources"})
    public R<?> menuNodeList() {
        return new R<>(service.menuNodeList());
    }

    @Override
    public void beforeSave(ResourceEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //key相同不添加
            if (service.count(new QueryWrapper<ResourceEntity>().eq("resource_key", entity.getResourceKey())) > 0) {
                throw new BusinessException(R.失败, "菜单key已存在");
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
    private ResourceServiceImpl service;

    @Override
    protected ResourceServiceImpl getService() {
        return service;
    }
}
