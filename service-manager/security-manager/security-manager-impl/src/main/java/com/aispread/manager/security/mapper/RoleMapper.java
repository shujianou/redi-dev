package com.aispread.manager.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aispread.manager.security.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {
    List<String> getRoleNameList(String userId);
}
