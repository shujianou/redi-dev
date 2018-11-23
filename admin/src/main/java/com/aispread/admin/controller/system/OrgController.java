package com.aispread.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.security.entity.OrgEntity;
import com.redimybase.manager.security.entity.RoleEntity;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.mapper.OrgMapper;
import com.redimybase.manager.security.service.impl.OrgServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 组织部门Controller
 * Created by Vim 2018/11/23 16:52
 *
 * @author Vim
 */
@RestController
@RequestMapping("org")
public class OrgController extends TableController<String, OrgEntity, OrgMapper, OrgServiceImpl> {

    @Override
    public void beforeSave(OrgEntity entity) {
        UserEntity userEntity = SecurityUtil.getCurrentUser();
        if (null == userEntity) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {
            //key相同不添加
            if (service.count(new QueryWrapper<OrgEntity>().eq("code", entity.getCode())) > 0) {
                throw new BusinessException(R.失败, "组织code已存在");
            }
            entity.setCreateTime(new Date());

            entity.setCreator(userEntity.getUserName());
        } else {
            entity.setReviser(userEntity.getUserName());
            entity.setReviserId(userEntity.getId());
            entity.setUpdateTime(new Date());
        }

        super.beforeSave(entity);
    }


    @Autowired
    private OrgServiceImpl service;
    @Override
    protected OrgServiceImpl getService() {
        return service;
    }
}
