package com.wwdj.admin.shiro;

import com.redimybase.security.shiro.constant.SecurityConst;
import com.wwdj.manager.security.token.UserCaptchaToken;
import com.wwdj.manager.security.token.UserToken;
import com.redimybase.security.utils.SecurityUtil;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.wwdj.manager.security.service.AppUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collection;

/**
 * Created by Irany 2018/5/13 12:10
 */
@Component
public class ShiroRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userId = (String) getAvailablePrincipal(principalCollection);

        if (StringUtils.isBlank(userId)) {
            return null;
        }

        AppUserEntity userEntity = appUserService.getById(userId);

        if (userEntity == null) {
            return null;
        }

        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String captcha = null;
        String clientCode = null;

        if (StringUtils.equals(useCaptcha,"1")) {
            if (token instanceof UserCaptchaToken) {
                captcha = ((UserCaptchaToken) token).getCaptcha();
                clientCode = ((UserCaptchaToken) token).getClientCode();

                if (captcha == null) {
                    captcha = "";
                }
            }
        }

        if (captcha != null) {
            if (StringUtils.isBlank(clientCode)) {
                logger.error("开启验证码功能,但未上传客户端标识:clientCode");
                throw new AuthenticationException(SecurityConst.验证码校验失败);
            }

            //redis取验证码(暂时不开启)
            //String relCaptchaCode = CaptchaUtils.getCaptcha(clientCode);
            String relCaptchaCode = "";
            if (!StringUtils.equals(captcha, relCaptchaCode)) {
                logger.error("验证码校验不正确,客户端标识：{},验证码：{}", clientCode, captcha);
                throw new AuthenticationException(SecurityConst.验证码校验失败);
            }
        }

        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());
        String md5PWD = password;

        if (StringUtils.equals(useSalt,"1")) {
            try {
                md5PWD = SecurityUtil.encryptPwd(password, pwdSalt);
            } catch (Exception e) {
                logger.error("验证用户时对用户密码进行MD5加密失败", e);
                throw new IncorrectCredentialsException(SecurityConst.用户登录校验失败);
            }
        }

        UserToken userToken = appUserService.getUserToken(username, md5PWD);

        if (userToken == null) {
            userToken = appUserService.getUserToken(username, password);

            if (userToken == null) {
                throw new AuthenticationException(SecurityConst.用户未注册);
            }
        }

        // 禁用的账号添加拦截
        if (!userToken.isEnabled()) {
            logger.warn("账号 {} 已经被禁用，尝试登录！", userToken.getAccount());
            throw new LockedAccountException(SecurityConst.用户被封禁);
        }
        return new SimpleAuthenticationInfo(userToken.getUserId(), password, getName());
    }

    protected Object getAvailablePrincipal(PrincipalCollection principalCollection) {
        Object primary = null;
        if (!org.apache.shiro.util.CollectionUtils.isEmpty(principalCollection)) {
            Collection thisPrincipals = principalCollection.fromRealm(getName());
            if (!org.apache.shiro.util.CollectionUtils.isEmpty(thisPrincipals)) {
                primary = thisPrincipals.iterator().next();
            } else {
                //no principals attributed to this particular realm.  Fall back to the 'master' primary:
                primary = principalCollection.getPrimaryPrincipal();
            }
        }

        return primary;
    }


    @Value("${redi.useCaptcha}")
    public String useCaptcha;

    @Value("${redi.pwd.useSalt}")
    public String useSalt;

    @Value("${redi.pwd.salt}")
    public String pwdSalt;

    @Autowired
    private AppUserService appUserService;
    private Logger logger = LoggerFactory.getLogger(getClass());
}
