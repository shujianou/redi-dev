package com.redimybase.framework.model.filter;

import java.io.Serializable;

/**
 * Created by Irany 2018/6/2 0:08
 */
public interface BeanFilter extends Serializable {
    String FILTER_PREFIX = "F_";

    /**
     * 过滤器标识符
     */
    String FILTER_SPLIT="-F-";

    /**
     * 添加过滤器
     * @param otClass
     * @param key
     * @param value
     */
    void addFilter(Class otClass, String key, String[] value);


    /**
     * 获取过滤器Provider
     * @return
     */
    ModelFilterProvider getProvider();
}
