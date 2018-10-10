package com.redimybase.security.shiro.filter;

import com.redimybase.security.shiro.dao.UserCheckDao;
import com.redimybase.security.shiro.token.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Irany 2018/5/14 0:45
 */
@Component
@Slf4j
public class ShiroFormFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect("/admin/main.html?bk=1");     //bk參數:是否刷新当前用户信息
        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse resp = (HttpServletResponse) response;
        try {
            resp.sendRedirect("/admin/login.html?type=" + e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated() && subject.isRemembered()) {
            String userId = (String) subject.getPrincipal();

            if (StringUtils.isNotEmpty(userId)) {
                UserToken token = userCheckDao.getByUserId(userId);

                if (token != null && token.isEnabled()) {
                    subject.login(new UsernamePasswordToken(token.getAccount(), token.getPassword(), true));
                    return subject.isRemembered();
                }
            }
        }
        return subject.isAuthenticated();
    }


    @Autowired
    private UserCheckDao userCheckDao;
}
