package com.redimybase.framework.model.datamodel.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.model.BaseModel;
import com.redimybase.framework.model.filter.JsonBean;
import com.redimybase.framework.mybatis.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * 默认数据模型实现类
 * Created by Irany 2018/6/1 18:09
 */
public class DefaultDataModel<E extends BaseEntity,ID extends Serializable> extends JsonBean implements BaseModel<E,ID> {
    /**
     * 获取数据
     *
     * @return 集合数据或分页
     */
    @Override
    public Object getData() {
        return data;
    }

    /**
     * 设置集合数据
     *
     * @param data 集合类数据
     */
    @Override
    public void setData(Collection<E> data) {
        this.data=data;
    }

    /**
     * 设置分页数据
     *
     * @param page 分页类数据
     */
    @Override
    public void setData(Page<E> page) {
        if (page!=null){
            setData(page.getRecords());
        }
    }

    /**
     * 替换数据
     *
     * @param data 要替换的集合类数据
     * @param page 分页数据
     */
    @Override
    public void setData(Collection<E> data, Page<E> page) {

    }

    /**
     * 设置Obj数据
     *
     * @param obj
     */
    @Override
    public void setData(Object obj) {

    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    protected Object data;

    /**
     * 当前页数
     */
    protected int page;

    /**
     * 每页数据数目
     */
    protected int pageSize;

    /**
     * 总页数
     */
    protected int total;

    /**
     * 数据总数目
     */
    protected int totalSize;



}
