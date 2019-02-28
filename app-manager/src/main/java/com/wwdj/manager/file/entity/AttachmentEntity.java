package com.wwdj.manager.file.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.redimybase.framework.mybatis.id.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 附件表
 * </p>
 *
 * @author vim
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_attachment")
public class AttachmentEntity extends IdEntity<String> {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 附件名称
     */
    @TableField("name")
    private String name;

    /**
     * 附件地址
     */
    @TableField("path")
    private String path;

    /**
     * 附件类型
     */
    private String type;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") private Date createTime;

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
     * 后缀类型
     */
    @TableField("suffix_type")
    private String suffixType;

    /**
     * 附件大小
     */
    private String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
