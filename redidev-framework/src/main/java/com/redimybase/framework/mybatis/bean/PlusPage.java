package com.redimybase.framework.mybatis.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * baomidou Page增强版
 * Created by Vim 2019/1/25 14:01
 *
 * @author Vim
 */
public class PlusPage<T> extends Page<T> {

    @Override
    @ApiIgnore
    public List<T> getRecords() {
        return super.getRecords();
    }

    @ApiModelProperty("根据什么字段排顺序")
    public String[] getAsc() {
        return ascs();
    }

    @ApiModelProperty("根据什么字段排倒序")
    public String[] getDesc() {
        return descs();
    }

    @Override
    @ApiModelProperty("当前页码")
    public long getCurrent() {
        return super.getCurrent();
    }

    @Override
    @ApiModelProperty("总数")
    public long getTotal() {
        return super.getTotal();
    }
}
