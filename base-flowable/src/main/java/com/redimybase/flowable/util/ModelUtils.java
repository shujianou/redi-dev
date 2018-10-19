package com.redimybase.flowable.util;

import lombok.extern.slf4j.Slf4j;
import org.flowable.app.domain.editor.AbstractModel;
import org.flowable.app.service.api.ModelService;
import org.flowable.app.service.exception.BaseModelerRestException;
import org.flowable.app.service.exception.InternalServerErrorException;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * Created by Vim 2018/10/19 17:34
 */
@Slf4j
@Component
public class ModelUtils {

    /**
     * 将Model转换为bpmn20xml
     *
     * @param model 流程模型
     */
    public byte[] generateBpmn20Xml(AbstractModel model) {
        if (model.getModelEditorJson() != null) {
            try {

                BpmnModel bpmnModel = modelService.getBpmnModel(model);
                return modelService.getBpmnXML(bpmnModel);

            } catch (BaseModelerRestException e) {
                throw e;

            } catch (Exception e) {
                log.error("Could not generate BPMN 2.0 XML", e);
                throw new InternalServerErrorException("Could not generate BPMN 2.0 xml");
            }
        }
        return null;
    }

    @Autowired
    private ModelService modelService;
}
