package com.redimybase.flowable.controller;

import com.alibaba.fastjson.JSONObject;
import org.flowable.app.security.SecurityUtils;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重定向Flowable用户登录地址
 * Created by Vim 2018/10/11 0011 0:24
 */
@RestController
@RequestMapping("flowable")
public class FlowableModelerUserController {

    @RequestMapping("account")
    public String account() {
        return JSONObject.toJSONString(SecurityUtils.getCurrentUserObject());
    }

}
