package com.wwdj.manager.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

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
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_item")
public class ItemEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品属性JSON
     */
    @TableField("attribute_json")
    private String attributeJson;

    /**
     * 卖家ID
     */
    @TableField("seller_id")
    private String sellerId;

    /**
     * 是否上架(0:下架,1:上架)
     */
    @TableField("up_shelf")
    private Integer upShelf;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 商品编号
     */
    @TableField("item_no")
    private String itemNo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
