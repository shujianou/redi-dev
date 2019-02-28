package com.wwdj.admin.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wwdj.manager.security.entity.AppUserEntity;
import com.wwdj.manager.security.service.AppUserService;
import com.wwdj.manager.test.service.impl.TestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Vim 2018/9/14 0:43
 */
@RestController
@RequestMapping( "test" )
@Slf4j
public class TestController {


    @GetMapping( "yes" )
    public String test() {
        AppUserEntity userEntity = userService.getOne(new QueryWrapper<AppUserEntity>().lambda().eq(AppUserEntity::getAccount, "admin"));
        return userEntity.getId();
    }

    @GetMapping( "log" )
    public String log() {
        Logger logger = LoggerFactory.getLogger(TestController.class);
        logger.info("访问{}", "log()");
        log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
        return logger.getClass().getName();
    }


    @Autowired
    private TestServiceImpl testService;

    @Autowired
    private AppUserService userService;
}