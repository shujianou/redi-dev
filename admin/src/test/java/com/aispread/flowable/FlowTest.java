package com.aispread.flowable;

import org.flowable.engine.*;
import org.flowable.engine.common.impl.util.IoUtil;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Vim 2018/10/12 15:29
 */
public class FlowTest {

    private ProcessEngine processEngine;

    private TaskService taskService;
    private RuntimeService runtimeService;
    private HistoryService historyService;
    private RepositoryService repositoryService;

    private FormService formService;
    private FormEngine formEngine;
    private FormRepositoryService formRepositoryService;

    @Before
    public void before() {
        //processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(FlowTest.class.getClassLoader().getResourceAsStream("flowable.cfg.xml")).buildProcessEngine();

        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml").buildProcessEngine();

        formEngine = FormEngineConfiguration.createFormEngineConfigurationFromResource("flowable.cfg.xml").buildFormEngine();
        //processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration().buildProcessEngine();

        taskService = processEngine.getTaskService();
        runtimeService = processEngine.getRuntimeService();
        historyService = processEngine.getHistoryService();
        repositoryService = processEngine.getRepositoryService();

        formService = processEngine.getFormService();
        formRepositoryService = formEngine.getFormRepositoryService();
    }

    @Test
    public void deployment() {
        InputStream inputStream = FlowTest.class.getClassLoader().getResourceAsStream("leave.bpmn");
        System.out.println(repositoryService.createDeployment().addBytes("leave.bpmn", IoUtil.readInputStream(inputStream, "leave.bpmn")).deploy());
    }


    @Test
    public void startProcessByKey() {
        String processKey = "myProcess";
        runtimeService.startProcessInstanceByKey(processKey);
        System.out.println("start process : "+processKey);
    }

    @Test
    public void deployForm() {

    }
}
