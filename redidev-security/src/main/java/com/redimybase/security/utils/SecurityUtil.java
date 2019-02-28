package com.redimybase.security.utils;

import com.redimybase.common.util.MD5;
import com.redimybase.framework.listener.SpringContextListener;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.wwdj.manager.security.service.AppUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Random;

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
     *
     * @return
     */
    public static AppUserEntity getCurrentUser() {
        String userId = getCurrentUserId();
        if (StringUtils.isNotEmpty(userId)) {
            return getUserService().getById(userId);
        }
        return null;
    }

    /**
     * 加盐加密
     */
    public static String encryptPwd(String pwd, String salt) throws Exception {
        return MD5.getInstance().getMD5to32(salt + pwd);
    }

    /**
     * 随机生成字母加数字的密码
     */
    public static String getResetPwd(int length) {

        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //length为几位密码
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }

    private static AppUserService getUserService() {
        return SpringContextListener.getBean(AppUserService.class);
    }

}
