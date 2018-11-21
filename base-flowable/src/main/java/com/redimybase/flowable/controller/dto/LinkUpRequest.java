package com.redimybase.flowable.controller.dto;

import lombok.Data;

/**
 * 沟通请求
 * Created by Vim 2018/11/21 15:10
 *
 * @author Vim
 */
@Data
public class LinkUpRequest {

    /**
     * 沟通人
     */
    private String userId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 文件ID列表
     */
    private String files;

    /**
     * 通知类型
     */
    private String noticeType;
}
