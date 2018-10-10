package com.redimybase.manager.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import com.redimybase.manager.security.entity.ResourceEntity;
import com.redimybase.security.dto.ResourceDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
public interface ResourceService extends IService<ResourceEntity> {

    List<String> getResKeyList(String userId);

    List<String> getResNameList(String userId);

    List<ResourceDto> getMenuByUserId(String currentUserId);

    /**
     * 获取所有菜单节点
     */
    List<Ztree> menuNodeList();
}
