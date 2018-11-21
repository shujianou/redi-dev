package com.redimybase.flowable.parser.handle;

import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.flowable.engine.parse.BpmnParseHandler;

import java.util.Collection;

/**
 * Created by Vim 2018/11/21 13:34
 *
 * @author Vim
 */
public class CustomUserTaskBpmnParseHandler implements BpmnParseHandler {
    @Override
    public Collection<Class<? extends BaseElement>> getHandledTypes() {
        return null;
    }

    @Override
    public void parse(BpmnParse bpmnParse, BaseElement element) {

    }


}
