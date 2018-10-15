package com.redimybase.flowable.controller;

import com.redimybase.framework.bean.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程监控Controller
 * Created by Vim 2018/10/12 0012 22:19
 */
@RestController
@RequestMapping("process/monitor")
public class ProcessMonitorController {

    /**
     * 获取所有流程实例
     */
    public R<?> queryInstance(Integer current, Integer size) {
        return new R<>();
    }



}
