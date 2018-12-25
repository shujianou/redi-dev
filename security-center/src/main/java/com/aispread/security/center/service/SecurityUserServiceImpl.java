package com.aispread.security.center.service;

import com.aispread.security.center.model.UserDetailInfo;
import com.aispread.service.api.security.UserCheckApi;
import com.aispread.manager.security.token.UserToken;
import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Vim 2018/11/25 1:09
 *
 * @author Vim
 */
@Service
public class SecurityUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserCheckApi userCheckApi;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserToken userToken = userCheckApi.getByAccount(s);
        if(null == userToken){
            throw new BusinessException(R.失败,"用户名或密码错误");
        }
        return new UserDetailInfo(userToken);
    }

}
