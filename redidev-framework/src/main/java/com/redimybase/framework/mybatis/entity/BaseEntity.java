package com.redimybase.framework.mybatis.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 基础实体类(所有的实体类都应继承该基础实体类)
 * Created by Irany 2018/5/11 0011 22:20
 */
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})  //忽略hibernate懒加载属性,防止转json失败
public interface BaseEntity<ID extends Serializable> extends Serializable {

    ID getId();

    void setId(ID id);
}
