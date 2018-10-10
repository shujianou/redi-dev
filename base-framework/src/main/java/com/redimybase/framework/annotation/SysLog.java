package com.redimybase.framework.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * Created by Irany 2018/7/30 17:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * 日志操作描述
     */
    String desc() default "";

    /**
     * 日志类型
     */
    String type() default "";

    /**
     * 是否启用日志
     */
    boolean enable() default true;
}
