package com.redimybase.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_var")
public class FlowVarEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 变量名
     */
    @TableField("name")
    private String name;

    /**
     * 变量key
     */
    @TableField("key")
    private String key;

    /**
     * 表单字段
     */
    @TableField("form_field")
    private String formField;

    /**
     * 变量类型(Number,Date,String)
     */
    @TableField("type")
    private String type;

    /**
     * 默认值
     */
    @TableField("default_value")
    private String defaultValue;

    /**
     * 变量表达式
     */
    @TableField("expression")
    private String expression;

    /**
     * 是否必填(0:否,1:是)
     */
    @TableField("required")
    private Integer required;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 节点ID
     */
    @TableField("node_id")
    private String nodeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
