package com.redimybase.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 流程操作相关
 * Created by Vim 2018/10/12 0012 21:52
 */
public interface FlowHandleService {

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


}
