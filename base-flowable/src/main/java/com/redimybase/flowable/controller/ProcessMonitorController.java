package com.redimybase.flowable.controller;

import com.redimybase.flowable.controller.dto.ProcessDefinitionDTO;
import com.redimybase.flowable.controller.dto.ProcessInstanceDTO;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import org.flowable.engine.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程监控Controller
 * Created by Vim 2018/10/12 0012 22:19
 */
@RestController
@RequestMapping("process/monitor")
public class ProcessMonitorController {

    /**
     * 获取所有流程实例
     */
    @RequestMapping("queryInstance")
    public R<?> queryInstance(Integer current, Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        return new R<>(instanceToDto(runtimeService.createProcessInstanceQuery().listPage(current, size)));
    }

    private List<ProcessInstanceDTO> instanceToDto(List<ProcessInstance> processInstances) {
        List<ProcessInstanceDTO> processInstanceDTOS = new ArrayList<>();

        processInstances.forEach(p -> {
            ProcessInstanceDTO dto = new ProcessInstanceDTO();
            dto.setActivityId(p.getActivityId());
            dto.setBusinessKey(p.getBusinessKey());
            dto.setDeploymentId(p.getDeploymentId());
            dto.setDescription(p.getDescription());
            dto.setEnded(p.isEnded());
            dto.setName(p.getName());
            dto.setParentId(p.getParentId());
            dto.setProcessDefinitionId(p.getProcessDefinitionId());
            dto.setProcessDefinitionKey(p.getProcessDefinitionKey());
            dto.setProcessDefinitionName(p.getProcessDefinitionName());
            dto.setProcessDefinitionVersion(p.getProcessDefinitionVersion());
            dto.setProcessInstanceId(p.getProcessInstanceId());
            dto.setStartTime(p.getStartTime());
            dto.setStartUserId(p.getStartUserId());
            processInstanceDTOS.add(dto);
        });
        return processInstanceDTOS;
    }

    /**
     * 获取所有流程定义
     */
    @RequestMapping("queryDef")
    public R<?> queryDef(Integer current, Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().listPage(current, size);

        return new R<>(defToDto(processDefinitions));
    }

    private List<ProcessDefinitionDTO> defToDto(List<ProcessDefinition> processDefinitions) {
        List<ProcessDefinitionDTO> processDefinitionDTOS = new ArrayList<>();
        processDefinitions.forEach(p -> {
            ProcessDefinitionDTO dto = new ProcessDefinitionDTO();
            dto.setCategory(p.getCategory());
            dto.setDeploymentId(p.getDeploymentId());
            dto.setDescription(p.getDescription());
            dto.setKey(p.getKey());
            dto.setName(p.getName());
            dto.setResourceName(p.getResourceName());
            dto.setVersion(p.getVersion());
            processDefinitionDTOS.add(dto);
        });
        return processDefinitionDTOS;
    }

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;


}
