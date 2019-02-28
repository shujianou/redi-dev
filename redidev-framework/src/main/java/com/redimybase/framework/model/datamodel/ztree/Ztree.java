package com.redimybase.framework.model.datamodel.ztree;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Irany 2018/6/16 1:09
 */
@Data
public class Ztree implements Serializable {
    private String id;      //节点ID
    @JsonProperty(value = "pId")
    private String pId;     //节点父ID
    private String name;    //节点名称
    private boolean open;   //是否展开
    private String url;     //节点链接
    private String icon;    //节点图标
    private String title;   //标题
}
