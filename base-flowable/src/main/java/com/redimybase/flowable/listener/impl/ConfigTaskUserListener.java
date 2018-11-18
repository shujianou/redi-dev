package com.redimybase.flowable.listener.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.flowable.listener.CustomTaskListener;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.entity.FlowUserEntity;
import com.redimybase.manager.flowable.service.FlowNodeService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.impl.el.ExpressionManager;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.Flowable5Util;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

/**
 * 配置任务用户监听器
 * Created by Vim 2018/11/17 14:34
 *
 * @author Vim
 */
public class ConfigTaskUserListener extends CustomTaskListener {

    @Override
    public void onCreate(DelegateTask task) throws Exception {

        FlowDefinitionEntity flowDefinitionEntity = flowDefinitionService.getOne(
                new QueryWrapper<FlowDefinitionEntity>().eq("flow_definition_id", task.getProcessDefinitionId()).select("id")
        );

        RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();

        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();

        FlowNodeEntity currentNode = flowNodeService.getOne(
                new QueryWrapper<FlowNodeEntity>().eq("definition_id", flowDefinitionEntity.getId())
                        .eq("task_code", executionEntity.getCurrentActivityId())
                        .select("id")
        );

        List<FlowUserEntity> currentConfigNodeUsers = flowUserService.list(
                new QueryWrapper<FlowUserEntity>().eq("node_id", currentNode.getId())
        );

        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();


        for (FlowUserEntity user : currentConfigNodeUsers) {
            String userIdExpression = expressionManager.createExpression(user.getValue()).getValue(task).toString();

            if (FlowUserEntity.Type.USER == user.getType()) {
                task.setAssignee(userIdExpression);
            }

            if (FlowUserEntity.Type.USER_GROUP == user.getType()) {
                task.addCandidateUser(userIdExpression);
            }
        }

    }

}
