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
 * 商品资源表
 * </p>
 *
 * @author vim
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_item_res")
public class ItemResEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 是否为主要资源(0:否,1:是)
     */
    @TableField("main")
    private Integer main;

    /**
     * 资源链接
     */
    @TableField("url")
    private String url;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 资源类型(1:图片,2:视频)
     */
    @TableField("type")
    private Integer type;

    /**
     * 状态(0:删除,1:正常)
     */
    @TableField("status")
    private Integer status;

    /**
     * 商品ID
     */
    @TableField("item_id")
    private String itemId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
