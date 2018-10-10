package com.aispread.admin.test.controller;

import com.aispread.manager.test.entity.TestEntity;
import com.aispread.manager.test.service.impl.TestServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Vim 2018/9/14 0:43
 */
@RestController
@RequestMapping("test")
@Slf4j
public class TestController {

    @GetMapping("yes")
    public String test() {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("user_name", "irany"));
        return userEntity.getId();
    }


    @Autowired
    private TestServiceImpl testService;

    @Autowired
    private UserService userService;
}