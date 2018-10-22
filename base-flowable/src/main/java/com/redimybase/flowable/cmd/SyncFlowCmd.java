package com.redimybase.flowable.cmd;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.common.impl.de.odysseus.el.tree.Node;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;

/**
 * 同步更新流程定义
 * Created by Vim 2018/10/22 17:43
 */
public class SyncFlowCmd implements Command<Void> {

    @Override
    public Void execute(CommandContext context) {

        return null;
    }

    /**
     * 配置节点
     */
    private void configNode(Node node, BpmnModel model){

    }

}
