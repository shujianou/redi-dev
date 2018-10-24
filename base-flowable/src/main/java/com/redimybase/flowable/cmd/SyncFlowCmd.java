package com.redimybase.flowable.cmd;

import com.redimybase.framework.listener.SpringContextListener;
import com.redimybase.manager.flowable.entity.FlowNodeEntity;
import com.redimybase.manager.flowable.service.FlowNodeService;
import com.redimybase.manager.flowable.service.impl.FlowNodeServiceImpl;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.common.impl.de.odysseus.el.tree.Node;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

import java.util.Collection;

/**
 * 同步更新流程定义
 * Created by Vim 2018/10/22 17:43
 */
public class SyncFlowCmd implements Command<Void> {

    public SyncFlowCmd(String processDefinitionId, String defId) {
        this.processDefinitionId = processDefinitionId;
        this.defId = defId;
        nodeService = SpringContextListener.getBean(FlowNodeServiceImpl.class);
    }

    @Override
    public Void execute(CommandContext context) {
        ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) context.getCurrentEngineConfiguration();

        //根据流程定义ID找到对应的流程模型
        BpmnModel model = processEngineConfiguration.getRepositoryService().getBpmnModel(processDefinitionId);

        Process process = model.getProcesses().get(0);

        //获取到模型里的流程节点
        Collection<FlowElement> flowElements = process.getFlowElements();

        //遍历
        flowElements.forEach(this::saveNode);
        return null;
    }


    /**
     * 保存节点
     * @param element 流程节点
     */
    private void saveNode(FlowElement element) {
        if (element == null) {
            return;
        }

        FlowNodeEntity nodeEntity;
        if (element instanceof SubProcess) {
            saveNode(element.getSubProcess());
        }

        nodeEntity = new FlowNodeEntity();
        nodeEntity.setName(element.getName());
        nodeEntity.setDefinitionId(defId);

        if (element instanceof UserTask) {
            nodeEntity.setType(FlowNodeEntity.Type.USER_TASK);
        }
        if (element instanceof StartEvent) {
            nodeEntity.setType(FlowNodeEntity.Type.START_EVENT);
        }
        if (element instanceof EndEvent) {
            nodeEntity.setType(FlowNodeEntity.Type.END_EVENT);
        }
        if (element instanceof Gateway) {
            nodeEntity.setType(FlowNodeEntity.Type.GATEWAY);
        }
        if (element instanceof ParallelGateway) {
            nodeEntity.setType(FlowNodeEntity.Type.PARALLEL_GATEWAY);
        }

        nodeService.save(nodeEntity);
    }

    /**
     * 配置节点
     */
    private void configNode(Node node, BpmnModel model) {

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

}
