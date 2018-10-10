package com.redimybase.framework.model.datamodel.ztree;

import lombok.Data;

import java.io.Serializable;

/**
 * ztree节点
 * Created by Irany 2018/6/2 13:27
 */
@Data
public class ZtreeNode<ID extends Serializable> implements Serializable {

    private ID id;      //节点ID
    private ID pId;     //节点父ID
    private String name;    //节点名称
    private boolean open;   //是否展开
    private String url;     //节点链接
    private String icon;    //节点图标
    private String title;   //标题

    public ZtreeNode(ID id, ID pId, String name, String url, String icon) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.url = url;
        this.icon = icon;
    }

    public ZtreeNode(ID id, ID pId, String name, String url) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.url = url;
    }

    public ZtreeNode(ID id, ID pId, String name, boolean open, String url, String icon) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.url = url;
        this.icon = icon;
    }

    public ZtreeNode(ID id, ID pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    public ZtreeNode(ID id, ID pId, String name, boolean open, String url, String icon, String title) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.open = open;
        this.url = url;
        this.icon = icon;
        this.title = title;
    }

    public ZtreeNode() {
    }
}
