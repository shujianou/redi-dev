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
 * 流程节点表
 * </p>
 *
 * @author vim
 * @since 2018-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_node")
public class FlowNodeEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 节点名称
     */
    @TableField("name")
    private String name;

    /**
     * 节点类型
     */
    @TableField("type")
    private String type;

    /**
     * 流程定义ID
     */
    @TableField("definition_id")
    private String definitionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public class Type{
        public static final String USER_TASK = "userTask";
        public static final String START_EVENT = "start";
        public static final String END_EVENT = "end";
        public static final String GATEWAY = "gateway";
        public static final String PARALLEL_GATEWAY = "parallelGateway";
    }
}
