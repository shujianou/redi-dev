package com.redimybase.security.shiro.token;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 带验证码的用户Token
 * Created by Irany 2018/5/13 14:41
 */
@Getter
@Setter
public class UserCaptchaToken extends UsernamePasswordToken {
    private String clientCode;
    private String captcha;

    public UserCaptchaToken(String clientCode, String captcha) {
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, char[] password, String clientCode, String captcha) {
        super(username, password);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, String password, String clientCode, String captcha) {
        super(username, password);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, char[] password, String host, String clientCode, String captcha) {
        super(username, password, host);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, String password, String host, String clientCode, String captcha) {
        super(username, password, host);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, char[] password, boolean rememberMe, String clientCode, String captcha) {
        super(username, password, rememberMe);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, String password, boolean rememberMe, String clientCode, String captcha) {
        super(username, password, rememberMe);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, char[] password, boolean rememberMe, String host, String clientCode, String captcha) {
        super(username, password, rememberMe, host);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }

    public UserCaptchaToken(String username, String password, boolean rememberMe, String host, String clientCode, String captcha) {
        super(username, password, rememberMe, host);
        this.clientCode = clientCode;
        this.captcha = captcha;
    }
}
