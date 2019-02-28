package com.wwdj.manager.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.wwdj.manager.security.mapper.AppUserMapper;
import com.wwdj.manager.security.service.AppUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwdj.manager.security.token.UserToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2019-02-14
 */
@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUserEntity> implements AppUserService {

    @Override
    public UserToken getUserToken(String username, String password) {
        AppUserEntity user;
        QueryWrapper<AppUserEntity> queryWrapper = new QueryWrapper<>();

        if (StringUtils.equals(useAccount, "1")) {
            queryWrapper.or().eq("account", username);
        }

        if (StringUtils.equals(useEmail, "1")) {
            queryWrapper.or().eq("email", username);
        }

        if (StringUtils.equals(usePhone, "1")) {
            queryWrapper.or().eq("phone", username);
        }

        if (StringUtils.equals(idNo, "1")) {
            queryWrapper.or().eq("id_no", username);
        }

        user = getOne(queryWrapper);

        if (user != null && StringUtils.equals(user.getPassword(), password)) {
            if (!AppUserEntity.Status.删除.equals(user.getStatus())) {
                return new UserToken(user.getId(), user.getAccount(), user.getPassword(), AppUserEntity.Status.启用.equals(user.getStatus()));
            }
        }
        return null;
    }

    @Override
    public UserToken getByUserId(String userId) {
        AppUserEntity user = getById(userId);

        if (user != null) {
            if (!AppUserEntity.Status.删除.equals(user.getStatus())) {
                UserToken token = new UserToken();
                token.setEnabled(AppUserEntity.Status.启用.equals(user.getStatus()));
                token.setAccount(user.getAccount());
                token.setPassword(user.getPassword());
                token.setUserId(user.getId());
                return token;
            }
        }
        return null;
    }

    @Value("${redi.idNo}")
    public String idNo;
    @Value("${redi.usePhone}")
    public String usePhone;

    @Value("${redi.useEmail}")
    public String useEmail;

    @Value("${redi.useAccount}")
    public String useAccount;

}
