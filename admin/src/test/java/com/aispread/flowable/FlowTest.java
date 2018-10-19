package com.aispread.flowable;

import org.flowable.app.domain.editor.Model;
import org.flowable.app.service.api.ModelService;
import org.flowable.app.service.editor.ModelServiceImpl;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.common.impl.util.IoUtil;
import org.flowable.form.api.FormDeployment;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.engine.FormEngine;
import org.flowable.form.engine.FormEngineConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private ModelService modelService;

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
        modelService = new ModelServiceImpl();
        formService = processEngine.getFormService();
        formRepositoryService = formEngine.getFormRepositoryService();
    }

    @Test
    public void deployment() {
        InputStream inputStream = FlowTest.class.getClassLoader().getResourceAsStream("leave.bpmn");
        System.out.println(repositoryService.createDeployment().addBytes("leave.bpmn", IoUtil.readInputStream(inputStream, "leave.bpmn")).deploy());
    }

    @Test
    public void deployByKey() {
        BpmnModel model = modelService.getBpmnModel(modelService.getModel("38ea3eb9-d375-11e8-8a50-54e1add943f5"));
        System.out.println(repositoryService.createDeployment().addBpmnModel("leave", model).deploy());
    }


    @Test
    public void startProcessByKey() {
        String processKey = "myProcess";
        runtimeService.startProcessInstanceByKey(processKey);
        System.out.println("start process : " + processKey);
    }

    @Test
    public void deployForm() {
        FormDeployment deployment = formRepositoryService.createDeployment().addFormBytes("testForm", formStr.getBytes()).deploy();
        System.out.println(deployment);
        FormDeployment deployment1=formRepositoryService.createDeploymentQuery().formDefinitionKey("testForm").singleResult();
    }


    private String formStr = "  {\n" +
            "\n" +
            "                \"key\": \"form1\",\n" +
            "\n" +
            "                \"name\": \"学生请假流程\",\n" +
            "\n" +
            "                \"fields\": [\n" +
            "\n" +
            "                                {\n" +
            "\n" +
            "                                \"id\": \"startTime\",\n" +
            "\n" +
            "                                \"name\": \"开始时间\",\n" +
            "\n" +
            "                                \"type\": \"text\",\n" +
            "\n" +
            "                                \"required\": false,\n" +
            "\n" +
            "                                \"placeholder\": \"empty\"\n" +
            "\n" +
            "                                },\n" +
            "\n" +
            "                                {\n" +
            "\n" +
            "                                \"id\": \"endTime\",\n" +
            "\n" +
            "                                \"name\": \"结束时间\",\n" +
            "\n" +
            "                                \"type\": \"text\",\n" +
            "\n" +
            "                                \"required\": false,\n" +
            "\n" +
            "                                \"placeholder\": \"empty\"\n" +
            "\n" +
            "                                },\n" +
            "\n" +
            "                                {\n" +
            "\n" +
            "                                \"id\": \"reason\",\n" +
            "\n" +
            "                                \"name\": \"请假原因\",\n" +
            "\n" +
            "                                \"type\": \"text\",\n" +
            "\n" +
            "                                \"required\": false,\n" +
            "\n" +
            "                                \"placeholder\": \"empty\"\n" +
            "\n" +
            "                                }\n" +
            "\n" +
            "                             ]\n" +
            "\n" +
            "                }";
}
