package com.wwdj.manager.item.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author vim
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_item_category")
public class ItemCategoryEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    @JsonProperty(value = "cat_id")
    private String id;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 别名(简称)
     */
    @TableField("short_name")
    @JsonProperty(value = "title")
    private String shortName;

    /**
     * 父级ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 排序,倒序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 状态(0:删除,1:正常)
     */
    @TableField("status")
    private Integer status;

    /**
     * 图片链接
     */
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status{
        public static final Integer 删除 = 0;
        public static final Integer 正常 = 1;

    }
}
