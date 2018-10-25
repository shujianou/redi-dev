package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.flowable.util.ModelUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.exception.BusinessException;
import com.redimybase.manager.flowable.entity.ReProcdefEntity;
import com.redimybase.manager.flowable.service.impl.ReProcdefServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.service.api.ModelService;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    @RequestMapping("def/list")
    public R<?> defList(Integer current, Integer size) {
        if (current == null) {
            throw new BusinessException(500, "缺少参数[current]");
        }
        if (size == null) {
            throw new BusinessException(500, "缺少参数[size]");
        }
        return new R<>(reProcdefService.page(new Page<>(current, size), new QueryWrapper<ReProcdefEntity>().select("key_", "name_", "category_", "id_")));
    }


    /**
     * 根据实例ID查看流程图
     *
     * @param processInstanceId 实例ID
     */
    @RequestMapping("view/flowPng")
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
    @RequestMapping("startProcessById")
    public R<?> startProcessByInstanceId(String id) {
        runtimeService.startProcessInstanceById(id);
        return R.ok();
    }

    /**
     * 根据流程定义KEY启动流程
     *
     * @param key 流程定义KEY
     */
    @RequestMapping("startProcessByKey")
    public R<?> startProcessByKey(String key) {
        runtimeService.startProcessInstanceByKey(key);
        return R.ok();
    }


    /**
     * 根据流程定义KEY启动表单流程(测试用)
     * @param key 流程定义KEY
     */
    @RequestMapping("startProcessWithFormByKey")
    public R<?> startProcessWithFormByKey(String key) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put("starTime", new Date());
        variables.put("entTime", new Date());
        variables.put("reason", "回家有事");
        runtimeService.startProcessInstanceWithForm(processDefinition.getId(), "suiyi", variables, "formInstance");
        return R.ok();
    }

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     */
    @RequestMapping("complete")
    public R<?> complete(String taskId) {
        taskService.complete(taskId);
        return R.ok();
    }

    /**
     * 根据流程定义ID部署流程
     *
     * @param processDefId 流程定义ID
     */
    @RequestMapping("deployByDefId")
    public R<?> deployByDefId(String processDefId) {
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

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelUtils modelUtils;

}
