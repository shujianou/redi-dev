package com.redimybase.framework.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.mybatis.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 数据模型渲染接口
 * Created by Irany 2018/6/1 17:47
 */
public interface BaseModel<E extends BaseEntity,ID extends Serializable> {

    /**
     * 获取数据
     * @return  集合数据或分页
     */
    Object getData();

    /**
     * 设置集合数据
     * @param data  集合类数据
     */
    void setData(Collection<E> data);

    /**
     * 设置分页数据
     * @param page  分页类数据
     */
    void setData(Page<E> page);

    /**
     * 替换数据
     * @param data  要替换的集合类数据
     * @param page  分页数据
     */
    void setData(Collection<E> data, Page<E> page);

    /**
     * 设置Obj数据
     * @param obj
     */
    void setData(Object obj);
}
