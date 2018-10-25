package com.redimybase.flowable.controller;

import com.redimybase.flowable.cmd.SyncFlowCmd;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.mapper.FlowDefinitionMapper;
import com.redimybase.manager.flowable.service.impl.FlowDefinitionServiceImpl;
import com.redimybase.security.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Vim 2018/10/24 17:52
 */
@RestController
@RequestMapping("flow/definition")
public class FlowDefinitionController extends TableController<String, FlowDefinitionEntity, FlowDefinitionMapper, FlowDefinitionServiceImpl> {


    @Override
    public R<?> save(FlowDefinitionEntity entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity.setUpdateTime(new Date());
        } else {
            entity.setCreateTime(new Date());
            entity.setCreator(Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUserName());
        }
        return super.save(entity);
    }

    @Override
    public void afterSave(FlowDefinitionEntity entity) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(entity.getFlowDefinitionKey()).latestVersion().singleResult();

        //同步流程定义信息
        managementService.executeCommand(new SyncFlowCmd(processDefinition.getId(), entity.getId()));

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
