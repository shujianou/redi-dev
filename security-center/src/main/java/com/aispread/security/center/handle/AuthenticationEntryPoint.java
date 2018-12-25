package com.aispread.security.center.handle;

import com.alibaba.fastjson.JSONArray;
import com.redimybase.common.framework.bean.R;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vim 2018/11/25 0:47
 *
 * @author Vim
 */
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONArray.toJSONString(new R<>(R.无权限, e.getMessage())));
        httpServletResponse.getWriter().flush();
    }
}
