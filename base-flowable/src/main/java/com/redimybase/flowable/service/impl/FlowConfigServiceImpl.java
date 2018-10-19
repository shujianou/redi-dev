package com.redimybase.flowable.service.impl;

import com.redimybase.flowable.service.FlowConfigService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Vim 2018/10/19 12:24
 */
@Service
public class FlowConfigServiceImpl implements FlowConfigService {
    @Override
    public Map<String, String> getGlobalVariables() {
        return null;
    }

    @Override
    public Map<String, String> getVariables() {
        return null;
    }

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;
}
