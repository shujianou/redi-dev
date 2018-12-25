package com.aispread.security.center.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.redimybase.common.framework.bean.R;

/**
 * Created by Vim 2018/11/25 0:22
 *
 * @author Vim
 */
@RestController
@RequestMapping("auth")
public class SecurityAuthController {

    @RequestMapping
    public R<?> auth(Authentication authentication) {
        if (null == authentication) {
            return new R<>(R.无权限, "权限受限");
        }
        return new R<>(authentication.getPrincipal());
    }

}
