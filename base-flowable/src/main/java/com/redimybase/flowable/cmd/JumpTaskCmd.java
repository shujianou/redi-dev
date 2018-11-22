package com.redimybase.flowable.cmd;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.history.HistoryManager;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;

/**
 * 任务自由跳转CMD
 * Created by Vim 2018/11/22 21:43
 *
 * @author Vim
 */
public class JumpTaskCmd implements Command<Void> {
    @Override
    public Void execute(CommandContext commandContext) {

        return null;
    }


    public void jump() {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();

        TaskEntityManager taskEntityManager = org.flowable.task.service.impl.util.CommandContextUtil.getTaskEntityManager();

        //当前任务
        TaskEntity currentTask = taskEntityManager.findById(taskId);

        //当前任务执行实例ID
        String currentExecutionId = currentTask.getExecutionId();

        //当前任务执行实例
        ExecutionEntity currentExecution = executionEntityManager.findById(currentExecutionId);


        FlowElement targetElement = ProcessDefinitionUtil.getProcess(currentTask.getProcessDefinitionId()).getFlowElement(targetElementId);

        //设置当前执行实例Element为目标Element
        currentExecution.setCurrentFlowElement(targetElement);

        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();

        agenda.planContinueProcessInCompensation(currentExecution);

        //删除当前任务节点
        taskEntityManager.delete(taskId);

        //记录原来的任务结束
        HistoryManager historyManager = CommandContextUtil.getHistoryManager();
        historyManager.recordTaskEnd(currentTask, currentExecution, "自由跳转");
        historyManager.recordActivityEnd(currentExecution, "自由跳转");
    }


    public JumpTaskCmd(String targetElementId, String taskId) {
        this.targetElementId = targetElementId;
        this.taskId = taskId;
    }

    /**
     * 跳转目标节点ID
     */
    private String targetElementId;

    /**
     * 任务ID
     */
    private String taskId;
}
