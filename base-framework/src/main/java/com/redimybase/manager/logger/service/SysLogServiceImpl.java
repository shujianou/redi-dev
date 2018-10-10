package com.redimybase.manager.logger.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.manager.logger.dto.SysLogCountDTO;
import com.redimybase.manager.logger.entity.SysLogEntity;
import com.redimybase.manager.logger.mapper.SysLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Irany 2018/7/30 16:57
 */
@Service//(value = "logService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper,SysLogEntity> implements SysLogService {
    @Override
    public String test(String arg) {
        return "日志测试,参数:"+arg;
    }

    @Override
    public List<SysLogCountDTO> countByCreateTime(String type) {
        return baseMapper.countByCreateTime(type);
    }

}
