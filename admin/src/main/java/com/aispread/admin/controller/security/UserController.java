package com.aispread.admin.controller.security;

import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.web.TableController;
import com.aispread.manager.security.entity.UserEntity;
import com.aispread.manager.security.mapper.UserMapper;
import com.aispread.manager.security.service.impl.UserServiceImpl;
import com.redimybase.security.shiro.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 用户信息Controller
 * Created by Vim 2018/11/23 13:30
 *
 * @author Vim
 */
@RestController
@RequestMapping("user")
public class UserController extends TableController<String, UserEntity, UserMapper, UserServiceImpl> {


    /**
     * 迁移用户(从一个组织迁移到另一个组织)
     */
    @PostMapping("move")
    public R<?> move(String sourceOrgId,String targetOrgId) {
        return R.ok();
    }

    @Override
    public void beforeSave(UserEntity entity) {
        UserEntity currentUser = SecurityUtil.getCurrentUser();
        if (null == currentUser) {
            throw new BusinessException(R.失败, "用户凭证过期");
        }
        if (StringUtils.isBlank(entity.getId())) {

            //如果密码为空默认123456
            if (StringUtils.isBlank(entity.getPassword())) {
                entity.setPassword(defaultPassword);
            }
            entity.setCreateTime(new Date());
            entity.setCreator(currentUser.getUserName());
            entity.setCreatorId(currentUser.getId());

        }else{
            entity.setUpdateTime(new Date());
            entity.setReviserId(currentUser.getId());
            entity.setReviser(currentUser.getUserName());
        }
        super.beforeSave(entity);
    }

    @Override
    public R<?> delete(String id) {
        if (StringUtils.isBlank(id)) {
            return R.fail();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setStatus(UserEntity.Status.DELETED);
        service.updateById(userEntity);
        return R.ok();
    }

    @Override
    public R<?> delete(List<String> ids) {
        for (String id : ids) {
            this.delete(id);
        }
        return R.ok();
    }

    @Value("${redi.default.init.password}")
    private String defaultPassword;

    @Autowired
    private UserServiceImpl service;
    @Override
    protected UserServiceImpl getService() {
        return service;
    }

}
