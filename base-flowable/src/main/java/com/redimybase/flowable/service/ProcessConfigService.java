package com.redimybase.flowable.service;

import java.util.Map;

/**
 * 流程配置相关Service
 * Created by Vim 2018/10/19 12:19
 */
public interface ProcessConfigService {


    /**
     * 获取任务的全局变量
     */
    @Deprecated
    public Map<String, String> getGlobalVariables();

    public Map<String, String> getVariables();
}
