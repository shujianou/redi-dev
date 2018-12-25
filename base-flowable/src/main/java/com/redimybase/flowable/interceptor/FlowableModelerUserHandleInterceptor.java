package com.redimybase.flowable.interceptor;

import com.aispread.manager.security.entity.UserEntity;
import com.redimybase.security.shiro.utils.SecurityUtil;
import org.flowable.app.security.SecurityUtils;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程设计器用户信息拦截器(对设计器session中的user赋值)
 * Created by Vim 2018/10/11 9:02
 */
@Component
public class FlowableModelerUserHandleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String servletPath = request.getServletPath();

        if (servletPath.endsWith(".css") || servletPath.endsWith(".js") || servletPath.endsWith(".jpg") || servletPath.endsWith(".png")) {
            return true;
        }

        System.out.println(servletPath);
        if (servletPath.startsWith("/app")) {
            UserEntity userEntity = SecurityUtil.getCurrentUser();
            if (userEntity == null) {
                return false;
            }
            User user = new UserEntityImpl();
            user.setId(userEntity.getAccount());
            SecurityUtils.assumeUser(user);
        }

        return true;
    }
}
