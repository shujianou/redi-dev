package com.redimybase.security.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.redimybase.common.enums.ErrorEnum;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.listener.SpringContextListener;
import com.wwdj.manager.security.service.AppUserService;
import com.wwdj.manager.security.token.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Irany 2018/5/14 0:45
 */
@Component
@Slf4j
public class ShiroFormFilter extends FormAuthenticationFilter {


    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSON.toJSONString(R.custom(ErrorEnum.无权限.getCode(), ErrorEnum.无权限.getDescription())));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse resp = (HttpServletResponse) response;
        //bk參數:是否刷新当前用户信息
        resp.sendRedirect("/admin/main.html?bk=1");
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
        HttpServletResponse httpResp = WebUtils.toHttp(response);
        HttpServletRequest httpReq = WebUtils.toHttp(request);

        //系统重定向会默认把请求头清空，这里通过拦截器重新设置请求头，解决跨域问题
        String origin = httpReq.getHeader("Origin");
        if (StringUtils.isBlank(origin)) {
            origin = httpReq.getHeader("Referer");
        }
        httpResp.setHeader("Access-Control-Allow-Origin", origin);
        httpResp.setHeader("Access-Control-Allow-Credentials", "true");
        httpResp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        httpResp.setHeader("Access-Control-Max-Age", "3600");
        httpResp.setHeader("Access-Control-Allow-Headers", "Content-type,x-requested-with,accept,cache-control,expires,pragma,Authorization,Cookie");
        httpResp.setHeader("Access-Control-Expose-Headers", "Set-Cookie");

        Subject subject = getSubject(request, response);

        if (!subject.isAuthenticated() && subject.isRemembered()) {
            String userId = (String) subject.getPrincipal();

            if (StringUtils.isNotEmpty(userId)) {
                if (appUserService == null) {
                    appUserService = SpringContextListener.getBean(AppUserService.class);
                }
                UserToken token = appUserService.getByUserId(userId);

                if (token != null && token.isEnabled()) {
                    subject.login(new UsernamePasswordToken(token.getAccount(), token.getPassword(), true));
                    return subject.isRemembered();
                }
            }
        }
        return subject.isAuthenticated();
    }


    private AppUserService appUserService;
}
