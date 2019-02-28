package com.redimybase.manager.logger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.manager.logger.dto.SysLogCountDTO;
import com.redimybase.manager.logger.entity.SysLogEntity;

import java.util.List;

/**
 * Created by Irany 2018/7/30 16:54
 */
public interface SysLogService extends IService<SysLogEntity> {
    String test(String arg);

    List<SysLogCountDTO> countByCreateTime(String type);
}
