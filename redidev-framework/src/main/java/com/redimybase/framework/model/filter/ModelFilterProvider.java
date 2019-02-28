package com.redimybase.framework.model.filter;


import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Model过滤器管理类
 * Created by Irany 2018/6/4 12:55
 */
public class ModelFilterProvider extends SimpleFilterProvider {


    /**
     * 增加过滤器
     *
     * @param clazz    过滤器
     * @param filterId 过滤器ID
     * @param property 过滤属性
     */
    public void addFilter(Class<?> clazz, String filterId, String... property) {
        String[] filter = filterId.split("_");

        if (filter.length == 2) {
            if ("in".equalsIgnoreCase(filter[1])) {
                _inFilter.put(filter[0], property);
            } else {
                _notInFilter.put(filter[0], property);
            }
        } else if (filter.length == 1) {
            if ("in".equalsIgnoreCase(filter[1])) {
                _inFilter.put(clazz.getName(), property);
            } else {
                _notInFilter.put(clazz.getName(), property);
            }
        }
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
        String[] filterIds = ((String) filterId).split(BeanFilter.FILTER_SPLIT);
        PropertyFilter f = null;

        if (filterIds != null && filterIds.length == 2) {
            f = _filtersById.get(filterIds[1]);

            if (f == null) {
                f = _filtersById.get(filterIds[0]);
            } else {
                //TODO 合并两个过滤器中的属性
            }
        } else if (filterIds != null && filterIds.length == 1) {
            f = _filtersById.get(filterId);
        }

        if (f == null) {
            f = _defaultFilter;
            if (f == null && _cfgFailOnUnknownId) {
                throw new IllegalArgumentException("No filter configured with id '" + filterId + "' (type "
                        + filterId.getClass().getName() + ")");
            }
        }
        return f;
    }


    /**
     * 获取过滤保留的属性
     * @param filterId  过滤器ID
     * @return
     */
    public String[] getInProperty(String filterId) {
        String[] filterIds = filterId.split(BeanFilter.FILTER_SPLIT);

        if (filterIds.length == 2) {
            if (_inFilter.get(filterIds[1]) == null) {
                return _inFilter.get(filterIds[0]);
            }
            return _inFilter.get(filterIds[1]);
        }
        return _inFilter.get(filterId);
    }

    /**
     * 过滤需要保留的属性
     */
    private Map<String, String[]> _inFilter = new HashMap<>();

    /**
     * 过滤排除的属性
     */
    private Map<String, String[]> _notInFilter = new HashMap<>();
}
