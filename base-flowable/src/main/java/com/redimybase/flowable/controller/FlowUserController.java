package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowUserEntity;
import com.redimybase.manager.flowable.mapper.FlowUserMapper;
import com.redimybase.manager.flowable.service.impl.FlowUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程用户controller
 * Created by Vim 2018/10/25 17:37
 */
@RestController
@RequestMapping("flow/user")
public class FlowUserController extends TableController<String, FlowUserEntity, FlowUserMapper, FlowUserServiceImpl> {


    /**
     * 根据节点ID获取审批用户信息
     */
    @PostMapping("queryByNodeId")
    public R<?> queryByNodeId(String nodeId) {
        return new R<>(service.list(new QueryWrapper<FlowUserEntity>().eq("node_id", nodeId)));
    }


    @Autowired
    private FlowUserServiceImpl service;

    @Override
    protected FlowUserServiceImpl getService() {
        return service;
    }
}
