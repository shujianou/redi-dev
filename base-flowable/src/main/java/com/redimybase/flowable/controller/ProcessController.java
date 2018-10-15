package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.redimybase.manager.flowable.entity.ReProcdefEntity;
import com.redimybase.manager.flowable.service.impl.ReProcdefServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程控制类
 * Created by Vim 2018/10/11 15:09
 */
@RestController
@RequestMapping("process")
@Slf4j
public class ProcessController {


    /**
     * 获取流程定义列表
     */
    @PostMapping("def/list")
    public R<?> defList(Integer current, Integer size) {
        return new R<>(reProcdefService.page(new Page<>(current, size), new QueryWrapper<ReProcdefEntity>().select("key_", "name_", "category_", "id_")));
    }


    /**
     * 根据实例ID查看流程图
     *
     * @param processInstanceId 实例ID
     */
    @GetMapping("view/flowPng")
    public void viewFlowPng(HttpServletResponse response, String processInstanceId) {

        //获取到相应实例的任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

        List<String> highLineActivity = new ArrayList<>();
        String processDefId;    //流程定义ID
        if (task == null) {
            //如果任务不存在则返回原版流程图
            processDefId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
        } else {
            //如果任务存在则返回跟踪流程图
            processDefId = task.getProcessDefinitionId();
            highLineActivity.add(task.getTaskDefinitionId());
        }

        //根据流程定义ID获取到对应的流程model
        BpmnModel model = repositoryService.getBpmnModel(processDefId);
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();

        try {
            IOUtils.copy(generator.generateDiagram(model, "png", highLineActivity), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("查看流程图出错,{}", e);
        }

    }


    /**
     * 根据流程ID启动流程
     *
     * @param id 流程ID
     */
    @PostMapping("startProcessById")
    public R<?> startProcessByInstanceId(String id) {
        runtimeService.startProcessInstanceById(id);
        return R.ok();
    }


    /**
     * 根据流程定义ID部署流程
     *
     * @param processDefId 流程定义ID
     */
    @PostMapping("deploy")
    public R<?> deployProcess(String processDefId) {
        BpmnModel model = repositoryService.getBpmnModel(processDefId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
        repositoryService.createDeployment().addBpmnModel(processDefinition.getResourceName(), model);
        return R.ok();
    }


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ReProcdefServiceImpl reProcdefService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;
}
