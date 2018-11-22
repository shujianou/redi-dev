package com.redimybase.framework.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.common.util.ReflectionUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.mybatis.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基层Controller
 * <p>所有的业务Controller都应继承基层Controller</p>
 * Created by Irany 2018/6/20 23:25
 */
public abstract class BaseController<ID extends Serializable, E extends BaseEntity<ID>, M extends BaseMapper<E>, S extends ServiceImpl<M, E>> {

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "save")
    public R<?> save(E entity) {
        beforeSave(entity);
        if (StringUtils.isBlank((String) entity.getId())) {
            getService().save(entity);
        } else {
            // TODO 如有涉及脏读数据再重写该方法加锁
            getService().updateById(entity);
        }
        afterSave(entity);
        return new R<>(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "delete")
    public R<?> delete(ID id) {
        beforeDelete(id);
        getService().removeById(id);
        afterDelete(id);
        return R.ok();
    }

    /**
     * 批量删除
     *
     * @param ids id表
     * @return
     */
    @PostMapping("deleteBatchIds")
    public R<?> delete(@RequestBody List<ID> ids) {
        getService().removeByIds(ids);
        return R.ok();
    }

    /**
     * 获取实体类详情
     *
     * @param id
     * @return
     */
    @PostMapping(value = "detail")
    public R<?> detail(ID id) {
        E entity = getService().getById(id);

        if (entity != null) {
            return new R<>(entity);
        }

        return new R<>(R.失败, "没有找到该实体");
    }


    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "query")
    public Object query(HttpServletRequest request) {
        TableModel<E> model = new TableModel<>();
        Page<E> page = (Page<E>) buildPageRequest(request);
        if (page == null) {
            page = new Page<>(1, 8);
        }
        model.setData(getService().page(page, buildPageWrapper(buildWrapper(getQueryColumn(request), getQueryValue(request)), getQueryKey(request), getQuerySearch(request))));
        return model;
    }


    /**
     * 取得带有相同前缀的Request 参数.
     *
     * @param request Request对象
     * @param prefix  参数前缀名称
     * @return 去除前缀的参数集合
     */
    private Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        return getParametersStartingWith(request, prefix, false);
    }

    /**
     * 取得带有相同前缀的Request 参数.
     *
     * @param request        Request对象
     * @param prefix         参数前缀名称
     * @param checkAttribute 是否检测Attribute中的参数
     * @return 去除前缀的参数集合
     */
    private Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix, boolean checkAttribute) {
        Validate.notNull(request, "Request must not be null");

        Map<String, Object> params = getParameters(request, prefix, false);

        if (checkAttribute) {
            params.putAll(getParameters(request, prefix, true));
        }

        return params;
    }

    private Map<String, Object> getParameters(ServletRequest request, String prefix, boolean fromAttribute) {
        Enumeration<String> paramNames = fromAttribute ? request.getAttributeNames() : request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();

        if (prefix == null) {
            prefix = "";
        }

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();

            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());

                // 处理Ajax上传数组数据时会在参数名后增加[]现象
                if (unprefixed.endsWith("[]")) {
                    unprefixed = unprefixed.substring(0, unprefixed.length() - 2);
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
     * 获取总数
     *
     * @return
     */
    protected Integer count() {
        return getService().count(new QueryWrapper<>());
    }


    /**
     * 构建查询Wrapper
     *
     * @param columns 列数组,例如AND_parent_id_EQ,则是and parent_id=
     * @param values  值数组
     * @return
     */
    protected QueryWrapper<E> buildWrapper(String[] columns, String[] values) {
        if (columns == null || values == null || columns.length < values.length) return null;

        QueryWrapper<E> wrapper = new QueryWrapper<>();
        for (int i = 0; i < columns.length; i++) {
            if (StringUtils.isBlank(columns[i])) continue;
            wrapper = analysisExpression(columns[i], values[i], wrapper);
        }
        return wrapper;
    }

    /**
     * 解析列表达式
     *
     * @param str
     * @param val
     * @param wrapper
     * @return
     */
    protected QueryWrapper<E> analysisExpression(String str, String val, QueryWrapper<E> wrapper) {
        String filter1 = str.substring(0, str.indexOf("_"));
        String filter2 = str.substring(str.lastIndexOf("_") + 1, str.length());
        String column = str.substring(str.indexOf("_") + 1, str.lastIndexOf("_"));

        switch (filter2) {
            case "EQ":
                filter2 = "=";
                break;
            case "NE":
                filter2 = "!=";
                break;
            case "LT":
                filter2 = "<";
                break;
            case "LE":
                filter2 = "<=";
                break;
            case "GT":
                filter2 = ">";
                break;
            case "GE":
                filter2 = ">=";
                break;
            case "LK":
                filter2 = "like";
                break;
            case "NK":
                filter2 = "not like";
            default:
                break;
        }

        if (StringUtils.equals("AND", filter1)) {
            String finalfilter2 = filter2;
            wrapper.and(w -> w.eq(column + finalfilter2 + "{0}", val));
        } else if (StringUtils.equals("OR", filter1)) {
            String finalfilter1 = filter2;
            wrapper.or(w -> w.eq(column + finalfilter1 + "{0}", val));
        }
        return wrapper;
    }

    /**
     * 构建分页Wrapper
     *
     * @return
     */
    protected Wrapper buildPageWrapper(QueryWrapper<E> wrapper, String[] keys, String search) {

        if (keys == null || search == null) {
            return wrapper;
        }
        if (wrapper == null) {
            wrapper = new QueryWrapper<>();
        }
        for (String key :
                keys) {
            if (StringUtils.isBlank(key)) continue;
            wrapper.or(w -> w.eq(key + " like {0}", "%" + search + "%"));
        }
        return wrapper;
    }

    /**
     * 构建分页参数
     *
     * @param request
     * @return
     */
    protected Page<?> buildPageRequest(HttpServletRequest request) {
        Integer pageNo = getPageNo(request);
        Integer pageSize = getPageSize(request);
        String pageOrderBy = getPageOderStr(request);
        boolean pageSort = getPageSort(request);

        if (pageNo == null && pageSize == null && pageOrderBy == null) {
            return null;
        }

        if (pageNo == null) {
            pageNo = 1;
        }

        if (pageSize == null) {
            pageSize = 8;
        }
        if (pageOrderBy == null) {
            return new Page<>(pageNo, pageSize);
        }

        if (pageSort) return new Page<>(pageNo, pageSize).setAsc(pageOrderBy);
        return new Page<>(pageNo, pageSize).setDesc(pageOrderBy);
    }


    /**
     * 从request获取排序方式(true:ASC,false:DESC),默认ASC
     *
     * @param request
     * @return
     */
    protected boolean getPageSort(HttpServletRequest request) {
        String sort = StringUtils.trim(request.getParameter(PAGE_SORT_PREFIX));
        return !StringUtils.isBlank(sort) && !StringUtils.equals(sort.toLowerCase(), "desc");
    }

    /**
     * 从request获取查询关键词
     *
     * @param request
     * @return
     */
    protected String[] getQueryColumn(HttpServletRequest request) {
        String keys = StringUtils.trim(request.getParameter(QUERY_COLUMN_PREFIX));
        return keys != null ? keys.split(",") : null;
    }


    /**
     * 从request获取查询值
     *
     * @param request
     * @return
     */
    protected String[] getQueryValue(HttpServletRequest request) {
        String keys = StringUtils.trim(request.getParameter(QUERY_VALUE_PREFIX));
        return keys != null ? keys.split(",") : null;
    }


    /**
     * 从request获取搜索关键词
     *
     * @param request
     * @return
     */
    protected String[] getQueryKey(HttpServletRequest request) {
        String keys = StringUtils.trim(request.getParameter(QUERY_KEY_PREFIX));
        return keys != null ? keys.split(",") : null;
    }

    /**
     * 从request获取搜索词
     *
     * @param request
     * @return
     */
    protected String getQuerySearch(HttpServletRequest request) {
        return StringUtils.trim(request.getParameter(QUERY_SEARCH_PREFIX));
    }

    /**
     * 从request获取排序字段
     *
     * @param request
     * @return
     */
    protected String getPageOderStr(HttpServletRequest request) {
        return StringUtils.trim(request.getParameter(PAGE_ORDER_BY_PREFIX));
    }

    /**
     * 从request获取当前页数
     *
     * @param request
     * @return
     */
    protected Integer getPageNo(HttpServletRequest request) {
        String pn = StringUtils.trim(request.getParameter(PAGE_NO_PREFIX));
        return StringUtils.isNumeric(pn) ? Integer.parseInt(pn, 10) : null;
    }

    /**
     * 从request获取数据显示大小
     *
     * @param request
     * @return
     */
    protected Integer getPageSize(HttpServletRequest request) {
        String pn = StringUtils.trim(request.getParameter(PAGE_SIZE_PREFIX));
        return StringUtils.isNumeric(pn) ? Integer.parseInt(pn, 10) : null;
    }

    /**
     * 前置保存
     */
    public void beforeSave(E entity) {
    }

    /**
     * 后置保存
     */
    public void afterSave(E entity) {
    }

    /**
     * 前置删除
     */
    public void beforeDelete(ID id) {
    }

    /**
     * 后置删除
     */
    public void afterDelete(ID id) {
    }

    protected abstract S getService();

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 当前页号
     */
    private static String PAGE_NO_PREFIX = "P_NO";
    /**
     * 分页大小
     */
    private static String PAGE_SIZE_PREFIX = "P_SIZE";
    /**
     * 根据XX字段排序
     */
    private static String PAGE_ORDER_BY_PREFIX = "P_SORT";
    /**
     * 排序方式
     */
    private static String PAGE_SORT_PREFIX = "P_ORDER";
    /**
     * 搜索KEY
     */
    private static String QUERY_KEY_PREFIX = "Q_KEY";
    /**
     * 搜索VALUE(关键词)
     */
    private static String QUERY_SEARCH_PREFIX = "Q_SEARCH";

    /**
     * 搜索KEY
     */
    private static String QUERY_COLUMN_PREFIX = "Q_COLUMN";
    /**
     * 搜索VALUE(关键词)
     */
    private static String QUERY_VALUE_PREFIX = "Q_VALUE";

    public String SECURITY_RESOURCE = "";


    private Class<E> entityClazz;

    public BaseController() {
        entityClazz = ReflectionUtils.getSuperClassGenricType(getClass(), 0);
    }
}
