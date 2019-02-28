package com.redimybase.framework.model;

import com.redimybase.framework.model.datamodel.ztree.ZtreeModel;
import com.redimybase.framework.mybatis.entity.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 数据模型工厂
 * Created by Irany 2018/6/1 17:45
 */
public class ModelFactory {


    /**
     * 转换为zTree模型
     *
     * @param clazz
     * @param mid
     * @param request
     * @param <EM>
     * @param <MID>
     * @return
     */
    public static <EM extends BaseEntity<MID>, MID extends Serializable> BaseModel<EM, MID> zTree(Class<EM> clazz, MID mid, HttpServletRequest request) {

        ZtreeModel<EM, MID> model = new ZtreeModel<>();

        model.setAttrName(request.getParameter(ZtreeModel.ATTR_PARAM));
        model.setIconName(request.getParameter(ZtreeModel.ICON_PARAM));
        model.setOpen(request.getParameter(ZtreeModel.OPEN_PARAM));
        model.setTitleName(request.getParameter(ZtreeModel.TITLE_PARAM));
        model.setUrlName(request.getParameter(ZtreeModel.URL_PARAM));

        return model;
    }
}
