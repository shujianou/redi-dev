package com.aispread.admin.controller.resource;

import com.redimybase.framework.bean.R;
import com.redimybase.manager.security.service.ResourceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源管理
 * Created by Irany 2018/6/15 23:21
 */
@RestController
@RequestMapping(value = "resource")
public class ResourceController {


    @PostMapping(value = "menuNodeList")
    @RequiresPermissions(value = {"system_resources"})
    public R<?> menuNodeList(){
        return new R<>(service.menuNodeList());
    }



    @Autowired
    private ResourceService service;
}
