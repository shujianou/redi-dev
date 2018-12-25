package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.mapper.FlowNodeMapper;
import com.redimybase.manager.flowable.service.impl.FlowNodeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程节点Controller
 * Created by Vim 2018/10/25 15:52
 */
@RestController
@RequestMapping("flow/node")
public class FlowNodeController extends TableController<String, FlowNodeEntity, FlowNodeMapper, FlowNodeServiceImpl> {


    @PostMapping("queryByDefinitionId")
    public R<?> queryByDefinitionId(String definitionId, String type) {
        if (StringUtils.isNotBlank(type)) {
            return new R<>(service.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId).eq("type", type)));
        }
        return new R<>(service.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId)));
    }


    @Autowired
    private FlowNodeServiceImpl service;

    @Override
    protected FlowNodeServiceImpl getService() {
        return service;
    }
}
