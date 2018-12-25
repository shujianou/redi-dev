package com.aispread.security.center.provider;

import com.aispread.manager.security.token.UserToken;
import com.aispread.security.center.model.UserDetailInfo;
import com.aispread.security.center.util.SecurityUtils;
import com.aispread.service.api.security.UserCheckApi;
import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by Vim 2018/11/27 0:05
 *
 * @author Vim
 */
@Slf4j
public class SecurityAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserToken userToken = userCheckApi.getByAccount(account);

        if (null == userToken) {
            throw new BusinessException(R.失败, "用户不存在");
        }
        String saltPassword;
        try {
            saltPassword = SecurityUtils.encryptPwd(password, userToken.getAccount());
        } catch (Exception e) {
            log.error("用户鉴权系统异常", e.getMessage());
            throw new BusinessException(R.失败, "用户鉴权系统异常");
        }
        if (saltPassword.equals(userToken.getPassword())) {
            UserDetailInfo userDetailInfo = new UserDetailInfo(userToken);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetailInfo.getUsername(), userDetailInfo.getPassword(), userDetailInfo.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
            return usernamePasswordAuthenticationToken;
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }


    public SecurityAuthenticationProvider(UserCheckApi userCheckApi) {
        this.userCheckApi = userCheckApi;
    }

    private UserCheckApi userCheckApi;
}
