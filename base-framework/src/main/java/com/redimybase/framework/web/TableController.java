package com.redimybase.framework.web;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.mybatis.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.Serializable;


/**
 * 前端Jqgrid控制器
 * Created by Irany 2018/6/20 23:31
 */
public abstract class TableController<ID extends Serializable,E extends BaseEntity<ID>, M extends BaseMapper<E>, S extends ServiceImpl<M, E>> extends BaseController<ID,E, M, S> {

    @PostMapping(value = "handle")
    public R<?> handle(String oper, E entity) {
        if (StringUtils.equals(oper, "save")) {
            this.save(entity);
        } else if (StringUtils.equals(oper, "del")) {
            this.delete(entity.getId());
        }
        return R.ok();
    }
}
