package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.common.framework.bean.R;
import com.redimybase.manager.flowable.entity.RuTaskEntity;
import com.redimybase.manager.flowable.service.RuTaskService;
import com.redimybase.security.shiro.utils.SecurityUtil;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程任务控制类
 * Created by Vim 2018/10/11 16:00
 */
@RestController
@RequestMapping("flowable/task")
public class TaskController {

    /**
     * 完成任务
     * @param taskId 任务ID
     */
    @PostMapping("complete")
    public R<?> complete(String taskId) {
        taskService.complete(taskId);
        return R.ok();
    }

    /**
     * 获取当前用户待办任务
     *
     * @param current 当前页
     * @param size    数量
     * @return
     */
    @PostMapping("list")
    public R<?> list(Integer current, Integer size) {
        return new R<>(
                ruTaskService.page(
                        new Page<>(current, size), new QueryWrapper<RuTaskEntity>()
                                .eq("assignee", SecurityUtil.getCurrentUserId())
                )
        );
    }


    @Autowired
    private FormService formService;

    @Autowired
    private RuTaskService ruTaskService;

    @Autowired
    private TaskService taskService;
}
