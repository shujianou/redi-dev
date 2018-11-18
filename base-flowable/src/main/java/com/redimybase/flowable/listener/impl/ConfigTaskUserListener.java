package com.redimybase.flowable.listener.impl;

import com.redimybase.flowable.listener.CustomTaskListener;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowDefinitionEntity;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.service.FlowNodeService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.Flowable5Util;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 配置任务用户监听器
 * Created by Vim 2018/11/17 14:34
 *
 * @author Vim
 */
public class ConfigTaskUserListener extends CustomTaskListener {

    @Override
    public void onCreate(DelegateTask task) throws Exception {
        RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();

        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();

        String businessKey = execution.getBusinessKey();

        String taskDefinitionKey = task.getTaskDefinitionKey();

        FlowDefinitionEntity definitionEntity = flowDefinitionService.getById(businessKey);
        FlowNodeEntity nodeEntity = flowNodeService.getById(businessKey);

    }

}
