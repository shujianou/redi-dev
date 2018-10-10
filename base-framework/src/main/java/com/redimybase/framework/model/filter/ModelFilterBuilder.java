package com.redimybase.framework.model.filter;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * 生产过滤器管理类
 * Created by Irany 2018/6/4 13:51
 */
public class ModelFilterBuilder {



    /**
     * 获取过滤器ID
     * @param clazz 实体类
     * @return
     */
    public static String getFilterId(Class clazz){
        JsonFilter jsonFilter= (JsonFilter) clazz.getAnnotation(JsonFilter.class);

        if (jsonFilter!=null){
            return jsonFilter.value()+BeanFilter.FILTER_SPLIT+clazz.getName();
        }
        return clazz.getName();
    }
}
