package com.redimybase.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.sql.Blob;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

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
 * @since 2018-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_de_model")
public class DeModelEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("name")
    private String name;

    @TableField("model_key")
    private String modelKey;

    @TableField("description")
    private String description;

    @TableField("model_comment")
    private String modelComment;

    @TableField("created")
    private LocalDateTime created;

    @TableField("created_by")
    private String createdBy;

    @TableField("last_updated")
    private LocalDateTime lastUpdated;

    @TableField("last_updated_by")
    private String lastUpdatedBy;

    @TableField("version")
    private Integer version;

    @TableField("model_editor_json")
    private String modelEditorJson;

    @TableField("thumbnail")
    private Blob thumbnail;

    @TableField("model_type")
    private Integer modelType;

    @TableField("tenant_id")
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
