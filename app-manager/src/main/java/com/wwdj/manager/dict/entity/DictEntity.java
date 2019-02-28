package com.wwdj.manager.dict.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  数据字典实体类
 * </p>
 *
 * @author vim
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_dict")
public class DictEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 类型code
     */
    @TableField("type_code")
    private String typeCode;

    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 字典KET
     */
    @TableField("dict_key")
    private String dictKey;

    /**
     * 字典值
     */
    @TableField("dict_value")
    private String dictValue;

    /**
     * 是否启用
     */
    @TableField("enable")
    private String enable;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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
     * 修改人ID
     */
    @TableField("modifier_id")
    private String modifierId;

    /**
     * 修改人
     */
    @TableField("modifier")
    private String modifier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
