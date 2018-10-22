package com.redimybase.flowable.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流程定义DTO
 * Created by Vim 2018/10/22 15:27
 * @author irany
 */
@Getter
@Setter
public class ProcessDefinitionDTO implements Serializable {

    protected String name;
    protected String description;
    protected String key;
    protected Integer version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;

    @Override
    public String toString() {
        return "ProcessDefinitionDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key + '\'' +
                ", version=" + version +
                ", category='" + category + '\'' +
                ", deploymentId='" + deploymentId + '\'' +
                ", resourceName='" + resourceName + '\'' +
                '}';
    }
}
