package com.redimybase.manager.security.entity;

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
 * 用户组织关联表
 * </p>
 *
 * @author vim
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_org")
public class UserOrgEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 组织ID
     */
    @TableField("org_id")
    private String orgId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
