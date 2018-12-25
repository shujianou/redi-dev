package com.aispread.service.api.security.fallback;

import com.aispread.manager.security.entity.UserEntity;
import com.aispread.service.api.security.UserCheckApi;
import com.aispread.manager.security.token.UserToken;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vim 2018/11/24 11:38
 *
 * @author Vim
 */
@Service
public class UserCheckApiFallback implements UserCheckApi {
    @Override
    public UserToken getByUserId(String userId) {
        return null;
    }

    @Override
    public UserToken getByAccount(String account) {
        return null;
    }

    @Override
    public List<String> getRoleNameList(String userId) {
        return null;
    }

    @Override
    public List<String> getResNameList(String userId) {
        return null;
    }

    @Override
    public List<String> getResKeyList(String userId) {
        return null;
    }

    @Override
    public UserToken getUserToken(UserToken userToken) {
        return null;
    }

    @Override
    public UserEntity getLoginInfo(String userId) {
        return null;
    }
}
