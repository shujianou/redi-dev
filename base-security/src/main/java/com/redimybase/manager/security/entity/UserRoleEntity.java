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
@TableName("t_user_role")
public class UserRoleEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
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
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String s) {
        this.id=s;
    }
}
