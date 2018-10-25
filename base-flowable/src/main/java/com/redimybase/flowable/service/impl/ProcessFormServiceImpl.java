package com.redimybase.flowable.service.impl;

import com.redimybase.flowable.service.ProcessFormService;
import org.flowable.engine.FormService;
import org.flowable.form.api.FormRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Vim 2018/10/19 12:50
 */
@Service
public class ProcessFormServiceImpl implements ProcessFormService {

    @Override
    public void deployForm() {
        formRepositoryService.createDeployment().addFormBytes("testForm", "".getBytes());
    }

    @Autowired
    private FormRepositoryService formRepositoryService;

    @Autowired
    private FormService formService;
}
