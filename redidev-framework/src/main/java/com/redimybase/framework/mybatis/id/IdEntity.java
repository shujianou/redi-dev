package com.redimybase.framework.mybatis.id;


import com.redimybase.framework.mybatis.entity.BaseEntity;

import java.io.Serializable;

/**
 * 统一ID抽象类
 * Created by Irany 2018/5/11 0011 22:21
 */
//@MappedSuperclass
public abstract class IdEntity<ID extends Serializable> implements BaseEntity<ID> {

    protected ID id;
}
