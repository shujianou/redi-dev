package com.redimybase.flowable.controller;

import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowVarEntity;
import com.redimybase.manager.flowable.mapper.FlowVarMapper;
import com.redimybase.manager.flowable.service.impl.FlowVarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程变量controller
 * Created by Vim 2018/10/25 17:04
 */
@RestController
@RequestMapping("flow/var")
public class FlowVarController extends TableController<String, FlowVarEntity, FlowVarMapper, FlowVarServiceImpl> {




    @Autowired
    private FlowVarServiceImpl service;

    @Override
    protected FlowVarServiceImpl getService() {
        return service;
    }
}
