package com.redimybase.manager.logger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redimybase.manager.logger.dto.SysLogCountDTO;
import com.redimybase.manager.logger.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * Created by Irany 2018/7/30 16:54
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLogEntity> {

    List<SysLogEntity> selectLogList();

    List<SysLogCountDTO> countByCreateTime(String type);
}
