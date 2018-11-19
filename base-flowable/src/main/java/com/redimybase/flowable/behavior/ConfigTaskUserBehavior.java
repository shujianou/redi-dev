package com.redimybase.flowable.behavior;

import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.common.impl.el.ExpressionManager;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.task.service.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.List;

/**
 * 任务节点创建前配置审批人
 * Created by Vim 2018/11/19 16:39
 */
public class ConfigTaskUserBehavior extends UserTaskActivityBehavior {

    public ConfigTaskUserBehavior(UserTask userTask) {
        super(userTask);
    }

    @Override
    protected void handleAssignments(TaskService taskService, String assignee, String owner, List<String> candidateUsers, List<String> candidateGroups, TaskEntity task, ExpressionManager expressionManager, DelegateExecution execution) {
        super.handleAssignments(taskService, assignee, owner, candidateUsers, candidateGroups, task, expressionManager, execution);
    }
}
