package com.aispread.manager.security.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Irany 2018/5/24 12:31
 */
@Data
public class ResourceInfo implements Serializable {

    private String name;
    private String icon;
    private String url;
    private String parentId;
    private String id;
    private Integer sort;

    private List<ResourceInfo> items;


    public ResourceInfo(String name, String icon, String url) {
        this.name = name;
        this.icon = icon;
        this.url = url;
    }

    public ResourceInfo(String icon, String url) {
        this.icon = icon;
        this.url = url;
    }

    public ResourceInfo() {
    }
}
