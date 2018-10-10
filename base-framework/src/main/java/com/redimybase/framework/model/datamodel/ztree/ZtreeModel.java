package com.redimybase.framework.model.datamodel.ztree;

import com.redimybase.framework.exception.BusinessException;
import com.redimybase.framework.model.datamodel.impl.DefaultDataModel;
import com.redimybase.framework.model.filter.ModelFilterBuilder;
import com.redimybase.framework.mybatis.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ztree树状图模型
 * Created by Irany 2018/6/1 23:32
 */
@Getter
@Setter
public class ZtreeModel<E extends BaseEntity<ID>, ID extends Serializable> extends DefaultDataModel<E, ID> {

    @Override
    public void setData(Collection<E> data) {

        if (data != null && data.size() > 0) {
            this.data = new ArrayList<ZtreeNode<ID>>(data.size());

            for (E entity :
                    data) {
                ((List<ZtreeNode<String>>) this.data).add(toItem(entity));
            }
        } else {
            this.data = new ArrayList<ZtreeNode<ID>>(1);
        }

    }


    /**
     * 转换为ztree对象
     *
     * @param entity 要转换的实体
     * @return
     */
    private ZtreeNode<String> toItem(E entity) {

        return new ZtreeNode<>(
                getVal("ID", entity),
                getVal("PARENT_ID", entity),
                getVal(attrName, entity),
                getVal(urlName, entity),
                getVal(iconName, entity)
        );

    }


    private void findProperty(E entity) {
        String[] filters = getProvider().getInProperty(ModelFilterBuilder.getFilterId(entity.getClass()));

    }


    private String getVal(String key, E entity) {
        if (StringUtils.isNotBlank(key)) {
            Class<?> toType = BeanUtils.findPropertyType(key, entity.getClass());

            try {
                return Ognl.getValue(key, entity, toType).toString();
            } catch (OgnlException e) {
                throw new BusinessException(500, "[Ztree]从实体类获取" + key + "的值失败");
            }
        }
        return null;
    }


    /**
     * 指定属性名称
     */
    public static final String ATTR_PARAM = "ZT_ATTR";

    /**
     * 指定图标属性名称
     */
    public static final String ICON_PARAM = "ZT_ICON";

    /**
     * 指定链接属性
     */
    public static final String URL_PARAM = "ZT_URL";

    /**
     * 指定标题属性
     */
    public static final String TITLE_PARAM = "ZT_TITLE";

    /**
     * 指定是否展开属性
     */
    public static final String OPEN_PARAM = "ZT_OPEN";


    /**
     * 属性名称
     */
    private String attrName;
    /**
     * 图标名称
     */
    private String iconName;
    /**
     * 链接名称
     */
    private String urlName;
    /**
     * 标题名称
     */
    private String titleName;

    /**
     * 是否展开
     */
    private String open;
}
