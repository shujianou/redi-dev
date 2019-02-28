package com.wwdj.manager.item.entity;

import java.math.BigDecimal;

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
 * 商品规格表
 * </p>
 *
 * @author vim
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_item_specs")
public class ItemSpecsEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 单品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品ID
     */
    @TableField("item_id")
    private String itemId;

    /**
     * 库存
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 商铺ID(0:自营)
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 单品规格JSON
     */
    @TableField("specs")
    private String specs;

    /**
     * 状态(0:删除,1:正常)
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
