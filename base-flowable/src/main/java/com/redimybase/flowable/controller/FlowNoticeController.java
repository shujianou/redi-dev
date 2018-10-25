package com.redimybase.flowable.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.web.TableController;
import com.redimybase.manager.flowable.entity.FlowNoticeEntity;
import com.redimybase.manager.flowable.mapper.FlowNoticeMapper;
import com.redimybase.manager.flowable.service.impl.FlowNoticeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程通知controller
 * Created by Vim 2018/10/25 18:06
 */
@RestController
@RequestMapping("flow/notice")
public class FlowNoticeController  extends TableController<String, FlowNoticeEntity, FlowNoticeMapper, FlowNoticeServiceImpl> {


    /**
     * 根据节点ID获取对应的通知信息
     */
    @PostMapping("listByNodeId")
    public R<?> listByNodeId(String nodeId) {
        return new R<>(service.list(new QueryWrapper<FlowNoticeEntity>().eq("node_id", nodeId)));
    }

    @Autowired
    private FlowNoticeServiceImpl service;

    @Override
    protected FlowNoticeServiceImpl getService() {
        return service;
    }
}
