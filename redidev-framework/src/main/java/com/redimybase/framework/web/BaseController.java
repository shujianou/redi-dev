package com.redimybase.framework.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redimybase.common.enums.ErrorEnum;
import com.redimybase.common.util.BizAssert;
import com.redimybase.common.util.ReflectionUtils;
import com.redimybase.framework.bean.R;
import com.redimybase.framework.model.datamodel.table.TableModel;
import com.redimybase.framework.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "通用保存", notes = "只能保存单个实体对象")
    public R<?> save(E entity) {
        beforeSave(entity);
        if (StringUtils.isBlank((String) entity.getId())) {
            getService().save(entity);
        } else {
            // TODO 如有涉及脏读数据再重写该方法加锁
            getService().updateById(entity);
        }
        afterSave(entity);
        return R.ok(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "delete")
    @ApiOperation(value = "通用单个删除", notes = "只能删除一个实体对象")
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
    @ApiOperation(value = "通用批量删除", notes = "能根据ID删除多个实体对象")
    public R<?> deleteBatchIds(@ApiParam(name = "ids", value = "对应的实体对象ID集合,格式为1,2,3,4,x,x", required = true) String ids) {
        String[] splitStr = StringUtils.split(ids, ",");
        getService().removeByIds(Arrays.asList(splitStr));
        return R.ok();
    }

    /**
     * 获取实体类详情
     *
     * @param id id
     * @return com.redimybase.framework.bean.BizResponse<?>
     * @author Charlie
     * @date 2019/2/15 19:53
     */
    @PostMapping(value = "detail")
    @ApiOperation(value = "获取实体类详情")
    public R<?> detail(ID id) {
        E entity = getService().getById(id);
        return BizAssert.of(entity)
                .ifNullThrow(ErrorEnum.未找到实体类)
                .mapTo(e -> R.ok(entity))
                .getData();
    }


    /**
     * 查询列表
     */
    @RequestMapping(value = "query",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "查询列表", notes = "可将进行分页,条件查询,参数如下:\n" +
            "[P_NO:当前页号,P_NO=1],\n" +
            "[P_SIZE:分页大小,P_SIZE=5]," +
            "[P_SORT:根据XX字段排序,P_SORT=create_time],\n" +
            "[P_ORDER:排序方式,P_ORDER=DESC],\n" +
            "[Q_COLUMN:搜索KEY],\n" +
            "[Q_VALUE:搜索VALUE(关键词)]")
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
        if (columns == null || values == null || columns.length < values.length) {
            return null;
        }

        QueryWrapper<E> wrapper = new QueryWrapper<>();
        for (int i = 0; i < columns.length; i++) {
            if (StringUtils.isBlank(columns[i])) {
                continue;
            }
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

        boolean andFlag = false;

        if (StringUtils.equals("AND", filter1)) {
            andFlag = true;
        }

        switch (filter2) {
            case "EQ":
                if (andFlag) {
                    wrapper.eq(column, val);
                } else {
                    wrapper.or(w -> w.eq(column, val));
                }
                break;
            case "NE":
                if (andFlag) {
                    wrapper.ne(column, val);
                } else {
                    wrapper.or(w -> w.ne(column, val));
                }
                break;
            case "LT":
                if (andFlag) {
                    wrapper.lt(column, val);
                } else {
                    wrapper.or(w -> w.lt(column, val));
                }
                break;
            case "LE":
                if (andFlag) {
                    wrapper.le(column, val);
                } else {
                    wrapper.or(w -> w.le(column, val));
                }
                break;
            case "GT":
                if (andFlag) {
                    wrapper.gt(column, val);
                } else {
                    wrapper.or(w -> w.gt(column, val));
                }
                break;
            case "GE":
                if (andFlag) {
                    wrapper.ge(column, val);
                } else {
                    wrapper.or(w -> w.ge(column, val));
                }
                break;
            case "LK":
                if (andFlag) {
                    wrapper.like(column, "%" + val + "%");
                } else {
                    wrapper.or(w -> w.like(column, "%" + val + "%"));
                }
                break;
            case "NK":
                if (andFlag) {
                    wrapper.and(w -> w.ne(column, val));
                } else {
                    wrapper.or(w -> w.ne(column, val));
                }
            default:
                break;
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
            if (StringUtils.isBlank(key)) {
                continue;
            }
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

        if (pageSort) {
            return new Page<>(pageNo, pageSize).setAsc(pageOrderBy);
        }
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
