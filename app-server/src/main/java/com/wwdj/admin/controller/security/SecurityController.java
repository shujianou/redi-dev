package com.wwdj.admin.controller.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.enums.ErrorEnum;
import com.redimybase.common.util.SequenceUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.common.exceptions.BizException;
import com.redimybase.security.shiro.constant.SecurityConst;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.wwdj.manager.security.service.AppUserService;
import com.wwdj.manager.security.token.UserToken;
import com.redimybase.security.utils.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 权限控制Controller
 * Created by Irany 2018/5/13 23:35
 */
@RestController
@RequestMapping("security")
@Api(tags = "权限控制接口")
public class SecurityController {


    /**
     * 权限校验
     */
    @ApiOperation(value = "权限校验")
    @RequestMapping(value = "auth")
    public R<?> auth(@ApiParam(value = "校验类型") String type) {
        try {

            String msg = "未登录的用户";

            Subject subject = SecurityUtils.getSubject();

            if ((subject != null && subject.isAuthenticated()) || StringUtils.isEmpty(type)) {
                //type = "2";
                return R.custom(ErrorEnum.无权限);
            }

            //int sign = 0;
            if (StringUtils.equals(SecurityConst.用户登录校验失败, type)) {
                msg = "用户名或密码错误";

            } else if (StringUtils.equals(SecurityConst.用户被封禁, type)) {
                msg = "用户被封禁";
            } else if (StringUtils.equals(SecurityConst.登录成功, type)) {
                return R.custom(ErrorEnum.无权限);
            }
            return R.custom(ErrorEnum.无权限);
        }catch (Exception e){
            throw  new BizException(ErrorEnum.无权限.getCode(),"用户凭证已过期");
        }
    }


    @ApiOperation(value = "登录")
    @RequestMapping("login")
    public R<?> login(@RequestBody UserToken token) {
        Subject subject = SecurityUtils.getSubject();

        try {
            if (token.isAutoReg()) {
                //如果是自动注册则自动创建账号
                if (appUserService.count(new QueryWrapper<AppUserEntity>().lambda().eq(AppUserEntity::getAccount, token.getAccount())) > 0) {
                    return R.fail("自动创建账号失败");
                }
                AppUserEntity appUserEntity = new AppUserEntity();
                appUserEntity.setAccount(token.getAccount());
                appUserEntity.setPassword("1".equalsIgnoreCase(useSalt) ? SecurityUtil.encryptPwd(token.getPassword(), pwdSalt) : token.getPassword());
                appUserEntity.setCreateTime(new Date());
                appUserEntity.setUserName(SequenceUtils.getSequenceInStr("会员"));
                appUserEntity.setStatus(AppUserEntity.Status.启用);
                appUserService.save(appUserEntity);
            }
            subject.login(new UsernamePasswordToken(token.getAccount(), token.getPassword(), true));
            Map<String, Object> reToken = new HashMap<>();
            reToken.put("token", subject.getSession().getId());
            return R.ok(reToken);
        } catch (IncorrectCredentialsException e) {
            return R.fail("用户名或密码错误");
        } catch (LockedAccountException e) {
            return R.fail("该用户已被冻结");
        } catch (AuthenticationException e) {
            return R.fail("该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.custom(404, "未知错误");
    }

    @RequestMapping(value = "info", name = "获取当前登录用户信息")
    @ApiOperation(value = "获取当前登录用户信息")
    public R<?> info() {
        R<Map<String, Object>> bizResponse = R.ok();
        Map<String, Object> data = new HashMap<>();
        data.put("user", SecurityUtil.getCurrentUser());
        bizResponse.setData(data);
        return bizResponse;
    }

    @Value("${redi.pwd.salt}")
    private String pwdSalt;

    @Value("${redi.pwd.useSalt}")
    private String useSalt;

    @Autowired
    private AppUserService appUserService;

}
