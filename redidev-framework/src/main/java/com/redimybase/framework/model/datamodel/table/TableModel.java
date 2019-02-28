package com.redimybase.framework.model.datamodel.table;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Irany 2018/6/20 23:29
 */

public class TableModel<T> {
    /** 数据. */
    @JsonProperty(value = "rows")
    protected Object data;
    /** 当前页号. */
    protected long page = 1;
    /** 每页记录数. */
    protected long pageSize;
    /** 总记录数. */
    @JsonProperty(value = "records")
    protected long total = 0;
    /** 总页数. */
    @JsonProperty(value = "totalPage")
    private long totalPages = 1L;

    //@JSONField(name = "rows")
    public Object getData() {
        return data;
    }

    /**
     * 获取当前页号.
     */
    public long getPage() {
        return page;
    }

    /**
     * 获取每页记录数.
     */
    public long getPageSize() {
        return pageSize==0?8:pageSize;
    }

    /**
     * 获取总记录数.
     */

    public long getTotal() {
        return total;
    }

    public void setTotal(Long total){
        this.total=total;
    }

    /**
     * 获取总页数.
     */

    public long getTotalPages() {
        return totalPages;
    }

    
    @JsonIgnore
    private boolean isUseDataOfRoot() {
        return false;
    }

    
    public void setData(List<T> data) {
        this.data = data;

        if (data != null) {
            this.pageSize = data.size();
            this.total = this.pageSize;
        }
    }



    public void setData(Page<T> page) {
        if (page != null) {
            setData(page.getRecords());
            this.page = page.getCurrent();
            this.pageSize = page.getSize();
            this.totalPages = page.getPages();
            this.total = page.getTotal();
        }
    }

    
    public void setData(Object data) {
        if (data instanceof List) {
            try {
                setData((List<T>) data);
            } catch (ClassCastException ex) {
                this.data = data;
            }
        } else if (data instanceof Page) {
            try {
                setData((Page<T>) data);
            } catch (ClassCastException ex) {
                this.data = data;
            }
        } else {
            this.data = data;
        }
    }

    
    public void setData(Page<T> page, List<T> data) {
        if (data == null || data.isEmpty()) {
            setData(page);
        } else if (page != null) {
            setData(data);
            this.page = page.getCurrent()+1;
            this.pageSize = page.getSize();
            this.totalPages = page.getPages();
            this.total = page.getTotal();
        }
    }

    
    @JsonIgnore
    public Object getResponse() {
        return isUseDataOfRoot()?data:this;
    }
}
