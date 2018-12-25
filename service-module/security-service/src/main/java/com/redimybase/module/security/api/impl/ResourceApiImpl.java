package com.redimybase.module.security.api.impl;

import com.aispread.manager.security.service.ResourceService;
import com.aispread.service.api.security.ResourceApi;
import com.aispread.manager.security.bean.ResourceInfo;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Vim 2018/11/24 2:26
 *
 * @author Vim
 */
@RestController
@RequestMapping("api/resource")
public class ResourceApiImpl implements ResourceApi {

    @Override
    @GetMapping("{userId}")
    public List<ResourceInfo> getMenuByUserId(@PathVariable("userId") String userId) {
        return resourceService.getMenuByUserId(userId);
    }

    @Override
    @GetMapping("resKeyList/{userId}")
    public List<String> getResKeyList(@PathVariable("userId") String userId) {
        return resourceService.getResKeyList(userId);
    }

    @Override
    @GetMapping("resNameList/{userId}")
    public List<String> getResNameList(@PathVariable("userId") String userId) {
        return resourceService.getResNameList(userId);
    }

    @Override
    @GetMapping("menuNodeList")
    public List<Ztree> menuNodeList() {
        return resourceService.menuNodeList();
    }



    @Autowired
    private ResourceService resourceService;
}
