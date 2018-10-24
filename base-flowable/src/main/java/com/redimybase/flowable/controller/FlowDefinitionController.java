package com.redimybase.flowable.controller;

import com.redimybase.flowable.cmd.SyncFlowCmd;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.mapper.FlowDefinitionMapper;
import com.redimybase.manager.flowable.service.impl.FlowDefinitionServiceImpl;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Vim 2018/10/24 17:52
 */
public class FlowDefinitionController extends TableController<String, FlowDefinitionEntity, FlowDefinitionMapper, FlowDefinitionServiceImpl> {


    @Override
    public void afterSave(FlowDefinitionEntity entity) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(entity.getFlowDefinitionKey()).singleResult();

        //同步流程定义信息
        managementService.executeCommand(new SyncFlowCmd(processDefinition.getId()));

        super.afterSave(entity);
    }

    @Autowired
    private FlowDefinitionServiceImpl service;

    @Override
    protected FlowDefinitionServiceImpl getService() {
        return service;
    }


    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private ManagementService managementService;
}
