package com.redimybase.common.util;

/**
 * 权限安全工具类
 * Created by Irany 2018/5/22 23:04
 */
public class SecurityUtils {

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

}
