package com.wwdj.manager.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

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
 * @since 2019-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_app_user")
public class AppUserEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 身份证
     */
    @TableField("id_no")
    private String idNo;

    /**
     * 生日
     */
    @TableField("birthday")
    private String birthday;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 性别(0:未知,1:男,2:女)
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 头像
     */
    private String face;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Status{
        public static final Integer 删除 = 0;
        public static final Integer 启用 = 1;
        public static final Integer 禁用 = 2;
    }

}
