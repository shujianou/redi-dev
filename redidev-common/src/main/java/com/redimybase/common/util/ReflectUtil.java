package com.redimybase.common.util;

import com.redimybase.common.util.support.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static jdk.nashorn.internal.runtime.PropertyDescriptor.GET;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/8/22 9:12
 */
public class ReflectUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger (ReflectionUtils.class);

    private static final String WRITE_REPLACE = "writeReplace";

    /**
     * 获取类的属性名
     *
     * @param field field
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/22 9:31
     */
    public static String fieldName(Property field) {
        String attr = null;
        try {
            Method method = field.getClass ().getDeclaredMethod (WRITE_REPLACE);
            method.setAccessible (Boolean.TRUE);
            SerializedLambda lambda = (SerializedLambda) method.invoke (field);
            String methodName = lambda.getImplMethodName ();
            if (methodName.startsWith (GET)) {
                attr = methodName.substring (3);
            }
            else {
                attr = methodName.substring (2);
            }
            //驼峰
            attr = Character.isLowerCase (attr.charAt (0)) ? attr : Character.toLowerCase (attr.charAt (0)) + attr.substring (1);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException (e.getMessage ());
        }
        return attr;
    }




    /**
     * 方法设为可用
     *
     * @param method method
     * @author Charlie
     * @date 2018/10/21 18:34
     */
    public static void makeAccessible(Method method) {
        ReflectionUtils.makeAccessible (method);
    }



}