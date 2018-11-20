package com.redimybase.flowable.parser.factory;

import com.redimybase.flowable.behavior.ConfigUserTaskBehavior;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

/**
 * 配置节点用户行为工厂
 * Created by Vim 2018/11/20 9:23
 * @author Vim
 */
public class ConfigTaskUserActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Override
    public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
        return new ConfigUserTaskBehavior(userTask);
    }
}
