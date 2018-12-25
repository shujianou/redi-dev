package com.aispread.security.center.handle;

import com.aispread.security.center.util.JwtTokenUtils;
import com.alibaba.fastjson.JSONArray;
import com.redimybase.common.framework.bean.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vim 2018/11/25 0:51
 *
 * @author Vim
 */
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        String acconut = (String) authentication.getPrincipal();
        String jwtToken = jwtTokenUtils.generateToken(acconut);
        jwtTokenUtils.setExpire(jwtToken, acconut, expiration + 100000);
        Map<String, String> rMap = new HashMap<>();
        rMap.put("token", jwtToken);
        httpServletResponse.getWriter().write(JSONArray.toJSONString(new R<>(rMap)));
        httpServletResponse.getWriter().flush();
    }

    @Value("${jwt.expiration}")
    private Long expiration;



    @Autowired
    private JwtTokenUtils jwtTokenUtils;
}
