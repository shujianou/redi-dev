package com.redimybase.framework.model;

import com.redimybase.common.util.SpringBeanUtils;
import com.redimybase.framework.model.datamodel.impl.DefaultDataModel;
import com.redimybase.framework.mybatis.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 框架数据模型处理器
 * Created by Irany 2018/6/1 17:44
 */
@Component
//@PropertySource(value = {"classpath:application-framework.properties"},encoding="utf-8")
public class FrameModelHandler extends AbstractFrameModelHandler {

    @Override
    public <EM extends BaseEntity<MID>, MID extends Serializable> BaseModel<EM, MID> getModelData(String frameName, String modelName, Class<EM> clazz, MID mid, HttpServletRequest request) {

        BaseModel<EM,MID> model = new DefaultDataModel();

        Object factory = getModelFactory(frameName);

        if (factory != null && StringUtils.isNotBlank(modelName)) {
            Method method = ReflectionUtils.findMethod(factory.getClass(), modelName, Class.class, Serializable.class, HttpServletRequest.class);

            model = (BaseModel<EM,MID>) ReflectionUtils.invokeMethod(method, factory, clazz, mid, request);
        }
        return model;
    }

    /**
     * 构建分页查询条件.
     * @param pageNumber 页号（从1开始）
     * @param pageSize 每页记录数
     * @param orderStrs 排序字段，格式为：字段名称,[(ASC,DESC)],[字段名称]...
     * @return 分页查询条件
     */
    public Pageable buildPageRequest(Integer pageNumber, Integer pageSize, String orderStrs, boolean returnDefaultIfNotPageParam) {
        Sort sort = getSort(orderStrs);

        if (pageNumber == null && pageSize == null && sort == null && !returnDefaultIfNotPageParam) {
            return null;
        }

        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = Integer.parseInt(defaultPageSize, 10);
        }

        if (sort == null) {
            return PageRequest.of(pageNumber-1,pageSize);
        }

        return PageRequest.of(pageNumber-1,pageSize,sort);
    }

    /**
     * 获取排序字段.
     * @param order 排序字段，格式为：字段名称,[(ASC,DESC)],[字段名称]...
     */
    public Sort getSort(String order) {
        Sort sort = null;

        if (StringUtils.isNotBlank(order)) {
            String[] orderStrs = order.split(",");
            List<Sort.Order> orders = new ArrayList<Sort.Order>(orderStrs.length);

            for (int i=0; i < orderStrs.length; i++) {
                String o = orderStrs[i];

                if (StringUtils.isNotBlank(o)) {
                    Sort.Direction direction = null;

                    if (i+1 < orderStrs.length && ("ASC".equalsIgnoreCase(orderStrs[i+1]) || "DESC".equalsIgnoreCase(orderStrs[i+1]))) {
                        direction = Sort.Direction.fromString(orderStrs[i+1]);
                        i++;
                    } else {
                        direction = Sort.DEFAULT_DIRECTION;
                    }

                    orders.add(new Sort.Order(direction, o));
                }
            }

            if (!orders.isEmpty()) {
                sort = new Sort(orders);
            }
        }

        return sort;
    }



    /**
     * 获取对应的模型工厂
     *
     * @param frameName 框架名称
     * @return
     */
    public Object getModelFactory(String frameName) {
        if (StringUtils.isNotBlank(frameName)) {
            return SpringBeanUtils.getBean(frameName + FACTORY_SUFFIX);
        }
        return null;
    }


    /**
     * 获取对应的模型工厂
     *
     * @param frameName 框架名称
     * @param clazz     工厂类型
     * @return
     */
    public Object getModelFactory(String frameName, Class<?> clazz) {
        if (StringUtils.isNotBlank(frameName)) {
            return SpringBeanUtils.getBean(frameName + FACTORY_SUFFIX, clazz);
        }
        return null;
    }




    @Value(value = "${redi.search.default.pagesize")
    public String defaultPageSize;



}
