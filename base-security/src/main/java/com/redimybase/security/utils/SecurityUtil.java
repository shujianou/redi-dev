package com.redimybase.security.utils;

import com.redimybase.common.util.MD5;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 权限安全工具类
 * Created by Irany 2018/5/22 23:04
 */
public class SecurityUtil {


    /**
     * 获取当前登录用户ID
     *
     * @return
     */
    public static String getCurrentUserId() {
        Subject subject = SecurityUtils.getSubject();

        if (subject != null && StringUtils.isNotEmpty(subject.getPrincipal().toString())) {
            return subject.getPrincipal().toString();
        }
        return null;
    }


    /**
     * 获取当前登录用户信息
     * @return
     */
    public static UserEntity getCurrentUser() {
        String userId = getCurrentUserId();
        if (StringUtils.isNotEmpty(userId)) {
            return getUserService().getById(userId);
        }
        return null;
    }

    /**
     * 加盐加密
     *
     * @param pwd
     * @param salt
     * @return
     * @throws Exception
     */
    public static String encryptPwd(String pwd, String salt) throws Exception {
        return MD5.getInstance().getMD5to32(salt + pwd);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(encryptPwd("123456","anly"));
    }


    private static UserService getUserService() {
        return SpringContextListener.getBean(UserService.class);
    }

}
