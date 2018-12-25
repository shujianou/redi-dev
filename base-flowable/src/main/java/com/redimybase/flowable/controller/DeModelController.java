package com.redimybase.flowable.controller;

import com.redimybase.common.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.DeModelEntity;
import com.redimybase.manager.flowable.mapper.DeModelMapper;
import com.redimybase.manager.flowable.service.impl.DeModelServiceImpl;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.service.api.ModelService;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工作流流程模型Controller
 * Created by Vim 2018/10/25 14:57
 */
@RestController
@RequestMapping("flowable/model")
public class DeModelController extends TableController<String, DeModelEntity, DeModelMapper, DeModelServiceImpl> {


    /**
     * 根据流程模型ID部署流程
     *
     * @param id 流程模型ID
     */
    @RequestMapping("deployByModelId")
    public R<?> deployByModelId(String id) {
        //方法一
        /*Model model = modelService.getModel(id);
        return new R<>(
                repositoryService.createDeployment().addBytes(model.getName() + ".bpmn", modelUtils.generateBpmn20Xml(model)).deploy()
        );*/
        //方法二
        Model model = modelService.getModel(id);
        repositoryService.createDeployment().addBpmnModel(
                model.getKey() + ".bpmn",
                modelService.getBpmnModel(model)).deploy();
        return R.ok();
    }


    @Override
    public R<?> save(DeModelEntity entity) {
        return R.fail("接口禁用");
    }

    @Override
    public R<?> delete(List<String> ids) {
        return R.fail("接口禁用");
    }

    @Override
    public R<?> delete(String s) {
        return R.fail("接口禁用");
    }

    @Autowired
    private ModelService modelService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DeModelServiceImpl service;

    @Override
    protected DeModelServiceImpl getService() {
        return service;
    }
}
