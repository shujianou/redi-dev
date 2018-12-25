package com.aispread.security.center.handle;

import com.alibaba.fastjson.JSONArray;
import com.redimybase.common.framework.bean.R;
import org.springframework.security.access.AccessDeniedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vim 2018/11/25 1:03
 *
 * @author Vim
 */
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONArray.toJSONString(new R<>(R.无权限,e.getMessage())));
        httpServletResponse.getWriter().flush();
    }
}
