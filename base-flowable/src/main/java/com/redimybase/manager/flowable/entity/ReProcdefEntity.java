package com.redimybase.manager.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author vim
 * @since 2018-10-11
 */
@Getter
@Setter
@TableName("act_re_procdef")
public class ReProcdefEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;

    @TableField("REV_")
    private Integer rev;

    @TableField("CATEGORY_")
    private String category;

    @TableField("NAME_")
    private String name;

    @TableField("KEY_")
    private String key;

    @TableField("VERSION_")
    private Integer version;

    @TableField("DEPLOYMENT_ID_")
    private String deploymentId;

    @TableField("RESOURCE_NAME_")
    private String resourceName;

    @TableField("DGRM_RESOURCE_NAME_")
    private String dgrmResourceName;

    @TableField("DESCRIPTION_")
    private String description;

    @TableField("HAS_START_FORM_KEY_")
    private Integer hasStartFormKey;

    @TableField("HAS_GRAPHICAL_NOTATION_")
    private Integer hasGraphicalNotation;

    @TableField("SUSPENSION_STATE_")
    private Integer suspensionState;

    @TableField("TENANT_ID_")
    private String tenantId;

    @TableField("ENGINE_VERSION_")
    private String engineVersion;

    @TableField("DERIVED_FROM_")
    private String derivedFrom;

    @TableField("DERIVED_FROM_ROOT_")
    private String derivedFromRoot;

    @TableField("DERIVED_VERSION_")
    private Integer derivedVersion;


    @Override
    public String getId() {
        return this.id;
    }
}
