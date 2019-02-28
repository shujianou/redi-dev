package com.redimybase.framework.model;

import com.redimybase.framework.mybatis.entity.BaseEntity;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * 框架模型数据抽象处理器
 * Created by Irany 2018/6/1 23:37
 */
public abstract class AbstractFrameModelHandler {


    public static final String FRAME_NAME = "S_frame";

    public static final String MODEL_NAME = "S_model";




    /**
     * 获取对应模型的数据
     *
     * @param <EM>
     * @param <MID>
     * @param frameName 框架名称
     * @param modelName 模型名称
     * @param clazz     转换对象
     * @param mid
     * @param request
     * @return
     */
    public  <EM extends BaseEntity<MID>, MID extends Serializable> BaseModel getModelData(String frameName, String modelName, Class<EM> clazz, MID mid, HttpServletRequest request) {
        return null;
    }



    /**
     * 取得带有相同前缀的Request 参数.
     * @param request Request对象
     * @param prefix 参数前缀名称
     * @return 去除前缀的参数集合
     */
    public Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        return getParametersStartingWith(request, prefix, false);
    }

    /**
     * 取得带有相同前缀的Request 参数.
     * @param request Request对象
     * @param prefix 参数前缀名称
     * @param checkAttribute 是否检测Attribute中的参数
     * @return 去除前缀的参数集合
     */
    public Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix, boolean checkAttribute) {
        Validate.notNull(request, "Request must not be null");

        Map<String, Object> params = getParameters(request, prefix, false);

        if (checkAttribute) {
            params.putAll(getParameters(request, prefix, true));
        }

        return params;
    }

    private Map<String, Object> getParameters(ServletRequest request, String prefix, boolean fromAttribute) {
        Enumeration<String> paramNames = fromAttribute?request.getAttributeNames():request.getParameterNames();
        Map<String, Object> params = new TreeMap<String,Object>();

        if (prefix == null) {
            prefix = "";
        }

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();

            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());

                // 处理Ajax上传数组数据时会在参数名后增加[]现象
                if (unprefixed.endsWith("[]")) {
                    unprefixed = unprefixed.substring(0, unprefixed.length()-2);
                }

                if (fromAttribute) {
                    params.put(unprefixed, request.getAttribute(paramName));
                } else {
                    String[] values = request.getParameterValues(paramName);
                    if (values == null || values.length == 0) {
                        // Do nothing, no values found at all.
                    } else if (values.length > 1) {
                        params.put(unprefixed, values);
                    } else {
                        params.put(unprefixed, values[0]);
                    }
                }
            }
        }

        return params;
    }

    /**
     * 构建分页查询条件.
     * @param pageNumber 页号（从1开始）
     * @param pageSize 每页记录数
     * @param orderStrs 排序字段，格式为：字段名称,[(ASC,DESC)],[字段名称]...
     * @return 分页查询条件
     */
    public Pageable buildPageRequest(Integer pageNumber, Integer pageSize, String orderStrs, boolean returnDefaultIfNotPageParam) {
      return null;
    }

    /**
     * 获取排序字段.
     * @param order 排序字段，格式为：字段名称,[(ASC,DESC)],[字段名称]...
     */
    public Sort getSort(String order) {
        return null;
    }


    /** 默认分页每页记录数参数名称. */
    public static final String DEFAULT_PAGESIZE_NAME = "P_rows";
    /** 默认分页页号参数名称. */
    public static final String DEFAULT_PAGE_NAME = "P_page";
    /** 默认排序参数名. */
    public static final String DEFAULT_SORT_NAME = "P_sort";

    /** 查询属性前缀. */
    public final static String SEARCH_PREFIX = "Q_";

    public static final String FACTORY_SUFFIX = "Factory";

}
