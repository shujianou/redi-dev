package com.aispread.security.center.handle;

import com.alibaba.fastjson.JSONArray;
import com.redimybase.common.framework.bean.R;
import org.springframework.security.core.Authentication;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vim 2018/11/25 1:01
 *
 * @author Vim
 */
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONArray.toJSONString(new R<>(R.成功,"用户已登出")));
        httpServletResponse.getWriter().flush();

    }
}
