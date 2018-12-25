package com.aispread.service.api.security;

import com.aispread.service.api.security.fallback.ResourceApiFallback;
import com.aispread.manager.security.bean.ResourceInfo;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by Vim 2018/11/24 11:51
 *
 * @author Vim
 */
@FeignClient(name = "security-service", fallback = ResourceApiFallback.class)
public interface ResourceApi {

    @GetMapping("resKeyList/{userId}")
    public List<String> getResKeyList(@PathVariable("userId") String userId);

    @GetMapping("resNameList/{userId}")
    public List<String> getResNameList(@PathVariable("userId") String userId);

    @GetMapping("{userId}")
    public List<ResourceInfo> getMenuByUserId(@PathVariable("userId") String userId);

    /**
     * 获取所有菜单节点
     */
    @GetMapping("menuNodeList")
    public List<Ztree> menuNodeList();
}
