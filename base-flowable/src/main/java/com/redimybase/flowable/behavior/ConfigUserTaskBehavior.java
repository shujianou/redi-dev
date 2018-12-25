package com.redimybase.flowable.behavior;

import com.aispread.manager.security.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.entity.FlowUserEntity;
import com.redimybase.manager.flowable.entity.FlowVarEntity;
import com.redimybase.manager.flowable.service.FlowDefinitionService;
import com.redimybase.manager.flowable.service.FlowNodeService;
import com.redimybase.manager.flowable.service.FlowUserService;
import com.redimybase.manager.flowable.service.FlowVarService;
import com.aispread.manager.security.entity.UserRoleEntity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.common.impl.el.ExpressionManager;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.task.service.TaskService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.List;

/**
 * 任务节点创建前配置流程信息
 * Created by Vim 2018/11/19 16:39
 *
 * @author Vim
 */
public class ConfigUserTaskBehavior extends UserTaskActivityBehavior {

    public ConfigUserTaskBehavior(UserTask userTask) {
        super(userTask);

        flowDefinitionService = SpringContextListener.getBean(FlowDefinitionService.class);
        flowNodeService = SpringContextListener.getBean(FlowNodeService.class);
        flowUserService = SpringContextListener.getBean(FlowUserService.class);
        flowVarService = SpringContextListener.getBean(FlowVarService.class);
        userRoleService = SpringContextListener.getBean(UserRoleService.class);

    }

    @Override
    public void execute(DelegateExecution execution) {
        super.execute(execution);
    }

    @Override
    protected void handleAssignments(TaskService taskService, String assignee, String owner, List<String> candidateUsers, List<String> candidateGroups, TaskEntity task, ExpressionManager expressionManager, DelegateExecution execution) {

        configUser(task);

        //configVar(task);

        super.handleAssignments(taskService, assignee, owner, candidateUsers, candidateGroups, task, expressionManager, execution);
    }


    /**
     * 配置审批人用户
     */
    private void configUser(TaskEntity task) {
        String processDefId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();

        FlowDefinitionEntity definitionId = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefId).select("id"));
        FlowNodeEntity nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId.getId()).eq("task_code", taskDefinitionKey).select("id"));
        List<FlowUserEntity> flowUserEntities = flowUserService.list(new QueryWrapper<FlowUserEntity>().eq("node_id", nodeId.getId()));

        for (FlowUserEntity flowUserEntity : flowUserEntities) {
            switch (flowUserEntity.getType()) {
                case FlowUserEntity.Type.INITIATOR:
                    task.setAssignee(task.getOwner());
                    break;
                case FlowUserEntity.Type.USER:
                    task.setAssignee(flowUserEntity.getValue());
                    break;
                case FlowUserEntity.Type.USER_GROUP:
                    List<UserRoleEntity> userRoleEntities = userRoleService.list(new QueryWrapper<UserRoleEntity>().eq("role_id", flowUserEntity.getValue()).select("user_id"));
                    for (UserRoleEntity userRoleEntity : userRoleEntities) {
                        task.addCandidateUser(userRoleEntity.getUserId());
                    }
                    break;
                case FlowUserEntity.Type.ORTHER_NODE:
                case FlowUserEntity.Type.FROM_FORM:
                    //来自节点或表单用户
                    task.addCandidateUser(flowUserEntity.getValue());
                    break;

                case FlowUserEntity.Type.LEADERSHIP:
                    //上级领导
                    break;
                default:
                    break;
            }


        }
    }

    /**
     * 配置流程变量
     */
    private void configVar(TaskEntity task) {
        String processDefId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();

        FlowDefinitionEntity definitionId = flowDefinitionService.getOne(new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", processDefId).select("id"));
        FlowNodeEntity nodeId = flowNodeService.getOne(new QueryWrapper<FlowNodeEntity>().eq("definition_id", definitionId.getId()).eq("task_code", taskDefinitionKey).select("id"));
        List<FlowVarEntity> flowVarEntities = flowVarService.list(new QueryWrapper<FlowVarEntity>().eq("node_id", nodeId.getId()));


    }

    private FlowDefinitionService flowDefinitionService;
    private FlowNodeService flowNodeService;
    private FlowUserService flowUserService;
    private FlowVarService flowVarService;
    private UserRoleService userRoleService;
}
