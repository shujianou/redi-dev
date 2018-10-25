package com.redimybase.manager.flowable.entity;

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
 * 流程通知表
 * </p>
 *
 * @author vim
 * @since 2018-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_flow_notice")
public class FlowNoticeEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 通知类型(站内信息:msg,电邮:email)
     */
    @TableField("notice_type")
    private String noticeType;

    /**
     * 接收人类型(0:普通用户,1:用户组)
     */
    @TableField("sendee_type")
    private Integer sendeeType;

    /**
     * 接收人
     */
    @TableField("sendee")
    private String sendee;
    /**
     * 通知模板key
     */
    @TableField("template_key")
    private String templateKey;

    /**
     * 通知过期时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 通知事件类型(arrival:到达,complete:完成,overtime:超时)
     */
    @TableField("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
