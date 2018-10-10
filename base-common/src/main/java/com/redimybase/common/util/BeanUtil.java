package com.redimybase.common.util;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.TypeFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 复制对象属性工具类
 * Created by Irany(欧书剑) 2017/8/17 0017 11:30
 */
@Component
@Slf4j
public class BeanUtil {
    /** 实例. */
    private static MapperFacade mapper;

    static {
        // 如果src中属性为null，就不复制到dest
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .mapNulls(false).build();
        // 如果属性是Object，就只复制引用，不复制值，可以避免循环复制
        mapperFactory.getConverterFactory().registerConverter(
                new PassThroughConverter(Object.class));

        mapper = mapperFactory.getMapperFacade();
    }

    /**
     * 把src中的值复制到dest中.
     */
    public static void copy(Object src, Object dest) {
        mapper.map(src, dest);
    }

    /**
     * 指定复制的src和target的class.
     */
    public static <S, D> void copy(S src, D target, Class<S> srcClass,
                            Class<D> targetClass) {
        mapper.map(src, target, TypeFactory.valueOf(srcClass),
                TypeFactory.valueOf(targetClass));
    }

    /**
     * 复制list.
     */
    public static  <S, D> List<D> copyList(List<S> src, Class<D> clz) {
        return mapper.mapAsList(src, clz);
    }


    public static void copyFields(Object source, Object target, String... fieldNames)
    {
        Assert.notNull(source,"source is null");
        Assert.notNull(target,"target is null");

        for (String fieldName : fieldNames)
        {
            try
            {
                Field field = FieldUtils.getField(source.getClass(), fieldName, true);
                field.setAccessible(true);
                field.set(target, field.get(source));
            }
            catch (Exception e)
            {
                log.warn(e.getMessage());
            }
        }
    }
}
