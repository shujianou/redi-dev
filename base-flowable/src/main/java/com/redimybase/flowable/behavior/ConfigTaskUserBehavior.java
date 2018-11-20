package com.redimybase.flowable.behavior;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.entity.FlowUserEntity;
import com.redimybase.manager.flowable.service.FlowDefinitionService;
import com.redimybase.manager.flowable.service.FlowNodeService;
import com.redimybase.manager.flowable.service.FlowUserService;
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
 * @author Vim
 */
public class ConfigTaskUserBehavior extends UserTaskActivityBehavior {

    public ConfigTaskUserBehavior(UserTask userTask) {
        super(userTask);
    }

    @Override
    protected void handleAssignments(TaskService taskService, String assignee, String owner, List<String> candidateUsers, List<String> candidateGroups, TaskEntity task, ExpressionManager expressionManager, DelegateExecution execution) {
        String processDefId = task.getProcessDefinitionId();
        String taskId = task.getId();

        FlowDefinitionService flowDefinitionService = SpringContextListener.getBean(FlowDefinitionService.class);
        FlowNodeService flowNodeService = SpringContextListener.getBean(FlowNodeService.class);
        FlowUserService flowUserService = SpringContextListener.getBean(FlowUserService.class);

        FlowDefinitionEntity definitionId = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefId).select("id"));
        FlowNodeEntity nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId.getId()).eq("task_code", taskId).select("id"));
        List<FlowUserEntity> flowUserEntities = flowUserService.list(new QueryWrapper<FlowUserEntity>().eq("node_id", nodeId.getId()));

        for (FlowUserEntity flowUserEntity : flowUserEntities) {
            task.addCandidateUser(flowUserEntity.getValue());
        }

        super.handleAssignments(taskService, assignee, owner, candidateUsers, candidateGroups, task, expressionManager, execution);
    }
}
