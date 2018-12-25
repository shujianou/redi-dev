package com.aispread.security.center.handle;

import com.alibaba.fastjson.JSONArray;
import com.redimybase.common.framework.bean.R;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vim 2018/11/25 0:49
 *
 * @author Vim
 */
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONArray.toJSONString(new R<>(R.失败,"用户名或密码不正确")));
        httpServletResponse.getWriter().flush();
    }
}
