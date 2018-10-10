package com.redimybase.framework.model.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;

/**
 * Created by Irany 2018/6/4 14:18
 */
public class JsonBean implements BeanFilter {
    /**
     * 添加过滤器
     *
     * @param otClass
     * @param key
     * @param value
     */
    @Override
    public void addFilter(Class otClass, String key, String[] value) {
        provider.addFilter(otClass, key, value);
    }


    @Override
    @Transient
    @JsonIgnore
    public ModelFilterProvider getProvider() {
        return provider;
    }

    /**
     * 过滤器管理类
     */
    private ModelFilterProvider provider = new ModelFilterProvider();

}
