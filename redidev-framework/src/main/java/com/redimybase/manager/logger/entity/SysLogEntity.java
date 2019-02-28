package com.redimybase.manager.logger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * Created by Irany 2018/7/30 16:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_log")
public class SysLogEntity extends IdEntity<String> {


    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    @TableField("user_name")
    private String userName;
    @TableField("operation")
    private String operation;
    @TableField("method")
    private String method;
    @TableField("params")
    private String params;
    @TableField("type")
    private String type;
    @TableField("ip")
    private String ip;
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;
}
