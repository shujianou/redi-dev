package com.redimybase.manager.security.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Getter;
import lombok.Setter;

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
@TableName("t_user")
public class UserEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 登录账号
     */
    @TableField("account")
    private String account;
    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;
    /**
     * 邮箱
     */
    @TableField("email")
    private String email;
    /**
     * 性别(0:男,1:女)
     */
    @TableField("sex")
    private Integer sex;
    /**
     * 头像(不建议外链)
     */
    @TableField("avatar_url")
    private String avatarUrl;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 身份证
     */
    @TableField("id_no")
    private String idNo;
    /**
     * 创建用户ID
     */
    @TableField("creator_id")
    private String creatorId;
    /**
     * 创建用户
     */
    @TableField("creator")
    private String creator;
    /**
     * 最后更新用户ID
     */
    @TableField("reviser_id")
    private String reviserId;
    /**
     * 最后更新用户
     */
    @TableField("reviser")
    private String reviser;
    /**
     * 状态(0:删除,1:启用,2:禁用)
     */
    @TableField("status")
    private String status;
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


    public static  class Status{
        public static final String DELETED = "0";
        public static final String ENABLE = "1";
        public static final String DISABLE = "2";
    }
}
