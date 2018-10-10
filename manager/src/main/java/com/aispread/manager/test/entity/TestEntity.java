package com.aispread.manager.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Getter;
import lombok.Setter;
/**
 * <p>
 * 
 * </p>
 *
 * @author vim
 * @since 2018-09-14
 */
@Getter
@Setter
@TableName("t_test")
public class TestEntity extends IdEntity<String> {


    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    @TableField("name")
    private String name;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
