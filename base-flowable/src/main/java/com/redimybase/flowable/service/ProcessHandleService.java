package com.redimybase.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 流程操作相关
 * Created by Vim 2018/10/12 0012 21:52
 */
public interface ProcessHandleService {

    /**
     * 获取所有流程实例
     */
    public Page<?> listInstance(Page page);


    /**
     * 获取所有待办任务
     */
    public Page<?> listTodoTask(Page page);

    /**
     * 获取所有流程定义
     */
    public Page<?> listDefinitions(Page page);


    /**
     * 获取所有代理中的任务
     */
    public Page<?> listAgentTask(String userId,Page page);

    /**
     * 获取所有已部署的流程
     */
    public Page<?> listDeployed(Page page);


    /**
     * 转办任务
     * @param taskId 任务ID
     * @param userId 转办用户ID
     */
    public void transfer(String taskId,String userId);


    /**
     * 与任务审批人沟通
     * @param taskId 任务ID
     * @param userId 审批人ID
     * @param noticeType 沟通类型
     */
    public void linkup(String taskId,String userId,String noticeType);

    /**
     * 终止任务
     * @param taskId 任务ID
     */
    public void stopTask(String taskId);

    /**
     * 恢复任务
     * @param taskId 任务ID
     */
    public void recoveryTask(String taskId);
}
