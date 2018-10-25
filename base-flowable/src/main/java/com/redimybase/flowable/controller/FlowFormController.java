package com.redimybase.flowable.controller;

import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowFormEntity;
import com.redimybase.manager.flowable.mapper.FlowFormMapper;
import com.redimybase.manager.flowable.service.impl.FlowFormServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程表单定义controller
 * Created by Vim 2018/10/25 16:05
 */
@RestController
@RequestMapping("flow/form")
public class FlowFormController extends TableController<String, FlowFormEntity, FlowFormMapper, FlowFormServiceImpl> {


    @Override
    public R<?> save(FlowFormEntity entity) {

        //如果有多个formKey则分开新增
        if (entity.getFormKey().contains(",")) {
            String[] keys = StringUtils.split(entity.getFormKey(), ",");
            for (String key : keys) {
                entity.setId(null);
                entity.setFormKey(key);
                super.save(entity);
            }
            return R.ok();
        }
        return super.save(entity);
    }

    @Autowired
    private FlowFormServiceImpl service;

    @Override
    protected FlowFormServiceImpl getService() {
        return service;
    }
}
