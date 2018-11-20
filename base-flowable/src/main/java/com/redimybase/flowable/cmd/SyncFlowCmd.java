package com.redimybase.flowable.cmd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.flowable.listener.impl.ConfigTaskUserListener;
import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowFormEntity;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.entity.FlowUserEntity;
import com.redimybase.manager.flowable.entity.FlowVarEntity;
import com.redimybase.manager.flowable.service.FlowFormService;
import com.redimybase.manager.flowable.service.FlowNodeService;
import com.redimybase.manager.flowable.service.FlowUserService;
import com.redimybase.manager.flowable.service.FlowVarService;
import com.redimybase.manager.flowable.service.impl.FlowFormServiceImpl;
import com.redimybase.manager.flowable.service.impl.FlowNodeServiceImpl;
import com.redimybase.manager.flowable.service.impl.FlowUserServiceImpl;
import lombok.experimental.var;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 同步更新流程定义
 * Created by Vim 2018/10/22 17:43
 */
public class SyncFlowCmd implements Command<Void> {

    public SyncFlowCmd(String processDefinitionId, String defId) {
        this.processDefinitionId = processDefinitionId;
        this.defId = defId;
        nodeService = SpringContextListener.getBean(FlowNodeServiceImpl.class);
        formService = SpringContextListener.getBean(FlowFormServiceImpl.class);
        userService = SpringContextListener.getBean(FlowUserServiceImpl.class);
        varService = SpringContextListener.getBean(FlowVarService.class);
    }

    @Override
    public Void execute(CommandContext context) {
        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) context.getCurrentEngineConfiguration();

        //根据流程定义ID找到对应的流程模型
        BpmnModel model = processEngineConfiguration.getRepositoryService().getBpmnModel(processDefinitionId);

        Process process = model.getProcesses().get(0);

        //获取到模型里的流程节点
        Collection<FlowElement> flowElements = process.getFlowElements();

        //遍历前清空对应流程定义的节点信息
        beforeSaveNode();
        flowElements.forEach(element -> {
            saveNode(element, null);
        });
        return null;
    }

    /**
     * 保存节点前删除无用数据
     */
    private void beforeSaveNode() {
        List<FlowNodeEntity> list = nodeService.list(new QueryWrapper<FlowNodeEntity>().eq("definition_id", defId).select("id"));

        for (FlowNodeEntity node : list) {
            formService.remove(new QueryWrapper<FlowFormEntity>().eq("node_id", node.getId()));
            userService.remove(new QueryWrapper<FlowUserEntity>().eq("node_id", node.getId()));
            varService.remove(new QueryWrapper<FlowVarEntity>().eq("node_id", node.getId()));
            nodeService.removeById(node.getId());
        }
    }


    /**
     * 保存节点
     *
     * @param element 流程节点
     */
    private void saveNode(FlowElement element, String subProcessId) {
        if (element == null) {
            return;
        }

        FlowNodeEntity nodeEntity;
        if (element instanceof SubProcess) {
            nodeEntity = new FlowNodeEntity();
            if (StringUtils.isNotBlank(element.getName())) {
                nodeEntity.setName(element.getName());
            } else {
                nodeEntity.setName("子流程");
            }
            nodeEntity.setType(FlowNodeEntity.Type.SUBPROCESS);
            nodeEntity.setDefinitionId(defId);
            nodeService.save(nodeEntity);

            //遍历子流程内的节点
            for (FlowElement flowElement : ((SubProcess) element).getFlowElements()) {
                saveNode(flowElement, nodeEntity.getId());
            }
        }

        nodeEntity = new FlowNodeEntity();
        nodeEntity.setName(element.getName());
        nodeEntity.setDefinitionId(defId);
        nodeEntity.setTaskCode(element.getId());
        if (subProcessId != null) {
            //子流程ID不为null说明为子流程内的节点
            nodeEntity.setParentId(subProcessId);
        }

        if (element instanceof UserTask) {
            nodeEntity.setType(FlowNodeEntity.Type.USER_TASK);
            confUserTask((UserTask) element, nodeEntity.getId());
        } else if (element instanceof StartEvent) {
            if (StringUtils.isBlank(nodeEntity.getName())) {
                nodeEntity.setName("开始节点");
            }
            nodeEntity.setType(FlowNodeEntity.Type.START_EVENT);
        } else if (element instanceof EndEvent) {
            if (StringUtils.isBlank(nodeEntity.getName())) {
                nodeEntity.setName("结束节点");
            }
            nodeEntity.setType(FlowNodeEntity.Type.END_EVENT);
        } else if (element instanceof InclusiveGateway) {
            nodeEntity.setType(FlowNodeEntity.Type.INCLUSIVE_GATEWAY);
        } else if (element instanceof ParallelGateway) {
            nodeEntity.setType(FlowNodeEntity.Type.PARALLEL_GATEWAY);
        } else {
            return;
        }
        nodeService.save(nodeEntity);
    }

    /**
     * 配置用户任务
     */
    private void confUserTask(UserTask userTask, String nodeId) {
        //配置表单
        if (StringUtils.isNotBlank(userTask.getFormKey())) {

            FlowFormEntity formEntity = new FlowFormEntity();
            formEntity.setFormKey(userTask.getFormKey());
            formEntity.setNodeId(nodeId);
            formEntity.setType(FlowFormEntity.Type.DEFAULT);

            formService.save(formEntity);
        }

        //配置用户
        if (StringUtils.isNotBlank(userTask.getAssignee())) {

            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.USER);
            userEntity.setValue(userTask.getAssignee());
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }
        List<String> tempUsers = userTask.getCandidateUsers();
        if (null != tempUsers && tempUsers.size() > 0) {
            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.USER);
            userEntity.setValue(StringUtils.join(tempUsers));
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }

        //配置用户组
        tempUsers = userTask.getCandidateGroups();
        if (null != tempUsers && tempUsers.size() > 0) {
            FlowUserEntity userEntity = new FlowUserEntity();
            userEntity.setSort(0);
            userEntity.setType(FlowUserEntity.Type.USER_GROUP);
            userEntity.setValue(StringUtils.join(tempUsers));
            userEntity.setNodeId(nodeId);

            userService.save(userEntity);
        }


    }


    /**
     * 业务流程定义ID
     */
    private String defId;

    /**
     * 工作流流程定义ID
     */
    private String processDefinitionId;

    private FlowNodeService nodeService;

    private FlowFormService formService;

    private FlowUserService userService;

    private FlowVarService varService;

}
