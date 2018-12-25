package com.aispread.manager.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import com.aispread.manager.security.entity.ResourceEntity;
import com.aispread.manager.security.mapper.ResourceMapper;
import com.aispread.manager.security.service.ResourceService;
import com.aispread.manager.security.bean.ResourceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author irany
 * @since 2018-08-07
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, ResourceEntity> implements ResourceService {

    @Override
    public List<String> getResKeyList(String userId) {
        return baseMapper.getResKeyList(userId);
    }

    @Override
    public List<String> getResNameList(String userId) {
        return baseMapper.getResNameList(userId);
    }

    @Override
    public List<ResourceInfo> getMenuByUserId(String userId) {

        List<ResourceEntity> entities = baseMapper.getResourceByUserId(userId);
        //对菜单权重排序
        entities.sort(comparator);

        return getChildren(null, entities);
    }


    /**
     * 获取所有菜单节点
     */
    public List<Ztree> menuNodeList() {
        return baseMapper.resourceTreeList();
    }


    /**
     * 获取子菜单
     *
     * @param entities
     * @return
     */
    private List<ResourceInfo> getChildren(String parentId, List<ResourceEntity> entities) {

        if (parentId == null) {
            menus.clear();
            entities.forEach(r -> {
                if (StringUtils.isEmpty(r.getParentId())) {
                    menus.add(toDto(r, entities));
                }
            });
        } else {
            List<ResourceInfo> children = new ArrayList<>();
            entities.forEach(r -> {
                if (StringUtils.equals(parentId, r.getParentId())) {
                    children.add(toDto(r, entities));
                }
            });
            return children;
        }

        return menus;
    }

    private ResourceInfo toDto(ResourceEntity r, List<ResourceEntity> entities) {
        ResourceInfo dto = JSONObject.parseObject(r.getConfigure(), ResourceInfo.class);
        dto.setName(r.getName());
        dto.setId(r.getId());
        dto.setParentId(r.getParentId());
        dto.setSort(r.getSort());
        dto.setItems(getChildren(r.getId(), entities));
        return dto;
    }

    /**
     * Resources排序工具类
     */
    private class ResourceSortComparator implements Comparator<ResourceEntity> {
        @Override
        public int compare(ResourceEntity o1, ResourceEntity o2) {
            if (o1 != null && o2 != null) {
                if (o1.getSort() != null && o2.getSort() != null) {
                    return o2.getSort().compareTo(o1.getSort());
                } else if (o1.getSort() != null && o2.getSort() == null) {
                    return -1;
                } else if (o1.getSort() == null && o2.getSort() != null) {
                    return 1;
                }
            }
            return 0;
        }
    }

    private Comparator<ResourceEntity> comparator = new ResourceSortComparator();

    private List<ResourceInfo> menus = new ArrayList<>();
}
