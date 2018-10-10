package com.redimybase.security.controller;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.security.service.ResourceService;
import com.redimybase.security.shiro.constant.SecurityConst;
import com.redimybase.security.shiro.dao.UserCheckDao;
import com.redimybase.security.shiro.token.UserToken;
import com.redimybase.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 权限控制Controller
 * Created by Irany 2018/5/13 23:35
 */
@RestController
@RequestMapping("security")
public class SecurityController {


    /**
     * 登录
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "auth")
    public R<?> auth(String type) {
        try {

            String msg = "未登录的用户";

            /*if (StringUtils.isEmpty(type)){
                response.sendRedirect("/admin/login.html");
            }*/

            Subject subject = SecurityUtils.getSubject();

            if ((subject != null && subject.isAuthenticated()) || StringUtils.isEmpty(type)) {
                //type = "2";
                return new R<>(R.无权限, msg);
            }

            //int sign = 0;
            if (StringUtils.equals(SecurityConst.用户登录校验失败, type)) {
                msg = "用户名或密码错误";

            } else if (StringUtils.equals(SecurityConst.用户被封禁, type)) {
                msg = "用户被封禁";
            } else if (StringUtils.equals(SecurityConst.登录成功, type)) {
                return new R<>(R.登录成功, "登录成功");
            }
            //response.sendRedirect("/admin/login.html?type="+type);
      /*  if (sign == 0) {
            try {
                response.sendRedirect("/admin/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect("/admin/main.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
            return new R<>(R.无权限, msg);
        }catch (Exception e){
            throw  new BusinessException(R.无权限,"用户凭证已过期");
        }
    }


    @RequestMapping("login")
    public R<?> login(UserToken token) {
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(token.getAccount(), token.getPassword(), true));
            Map<String, Object> reToken = new HashMap<>();
            reToken.put("token", subject.getSession().getId());
            return new R<>(reToken);
        } catch (IncorrectCredentialsException e) {
            return R.custom(R.无权限,"用户名或密码错误");
        } catch (LockedAccountException e) {
            return R.custom(R.无权限,"该用户已被冻结");
        } catch (AuthenticationException e) {
            return R.custom(R.无权限,"该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.custom(404, "未知错误");
    }

    @RequestMapping(value = "info", name = "获取当前登录用户信息")
    public R<?> info() {
        R<Map<String, Object>> result = new R<>();
        Map<String, Object> data = new HashMap<>();
        data.put("user", SecurityUtil.getCurrentUser());
        data.put("resources", resourceService.getMenuByUserId(SecurityUtil.getCurrentUserId()));
        result.setData(data);
        return result;
    }


    @Autowired
    private UserCheckDao userCheckDao;

    @Autowired
    private ResourceService resourceService;

}
