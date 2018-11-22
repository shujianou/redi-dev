package com.redimybase.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Getter
@Setter
@TableName("t_resource")
public class ResourceEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 资源KEY
     */
    @TableField("resource_key")
    private String key;
    /**
     * 资源名称
     */
    @TableField("name")
    private String name;
    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 配置
     */
    @TableField("configure")
    private String configure;
    /**
     * 类型(1:菜单,2:按钮)
     */
    @TableField("type")
    private Integer type;
    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private String creatorId;
    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;
    /**
     * 最后修改人ID
     */
    @TableField("reviser_id")
    private String reviserId;
    /**
     * 最后修改人
     */
    @TableField("reviser")
    private String reviser;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String s) {
        this.id=s;
    }
}
