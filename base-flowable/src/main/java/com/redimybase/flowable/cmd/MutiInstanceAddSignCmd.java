package com.redimybase.flowable.cmd;

import org.flowable.bpmn.model.Activity;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.util.CommandContextUtil;

import java.util.HashMap;

/**
 * 多实例任务加签CMD
 * Created by Vim 2018/11/23 12:49
 *
 * @author Vim
 */
public class MutiInstanceAddSignCmd implements Command<Void> {

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntity currentTask = CommandContextUtil.getTaskEntityManager().findById(taskId);

        ExecutionEntity currentExecution = org.flowable.engine.impl.util.CommandContextUtil.getExecutionEntityManager().findById(currentTask.getExecutionId());

        FlowElement flowElement = ProcessDefinitionUtil.getProcess(processDefinitionId).getFlowElement(currentTask.getTaskDefinitionKey());

        RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();

        runtimeService.addMultiInstanceExecution(flowElement.getId(),currentExecution.getId(),new HashMap<>());
        return null;
    }


    public MutiInstanceAddSignCmd(String taskId, String processDefinitionId) {
        this.taskId = taskId;
        this.processDefinitionId = processDefinitionId;
    }

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

}
