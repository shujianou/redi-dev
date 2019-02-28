package com.wwdj.manager.banner.entity;

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
 * APP轮播图表
 * </p>
 *
 * @author vim
 * @since 2019-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_app_banner")
public class AppBannerEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 轮播图名称
     */
    @TableField("name")
    private String name;

    /**
     * 轮播图URL
     */
    @TableField("url")
    private String url;

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
    private Integer status;

    /**
     * 跳转URL
     */
    private String src;
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
