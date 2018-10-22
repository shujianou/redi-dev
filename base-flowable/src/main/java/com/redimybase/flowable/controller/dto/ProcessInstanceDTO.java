package com.redimybase.flowable.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例DTO
 * Created by Vim 2018/10/22 15:49
 * @author irany
 */
@Getter
@Setter
public class ProcessInstanceDTO implements Serializable {

    protected String name;
    protected String description;
    protected Boolean ended;
    protected String startUserId;
    protected Date startTime;

    protected String processDefinitionId;

    protected String processDefinitionKey;

    protected String processDefinitionName;

    protected Integer processDefinitionVersion;

    protected String deploymentId;

    protected String activityId;

    protected String processInstanceId;

    protected String businessKey;

    protected String parentId;

    @Override
    public String toString() {
        return "ProcessInstanceDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ended=" + ended +
                ", startUserId='" + startUserId + '\'' +
                ", startTime=" + startTime +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionName='" + processDefinitionName + '\'' +
                ", processDefinitionVersion=" + processDefinitionVersion +
                ", deploymentId='" + deploymentId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
