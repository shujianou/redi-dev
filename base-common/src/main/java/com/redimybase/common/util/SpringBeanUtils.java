package com.redimybase.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 获取Spring已注册的Bean
 * Created by Irany 2018/5/22 23:16
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (SpringBeanUtils.applicationContext == null) {

            SpringBeanUtils.applicationContext = applicationContext;

        }

    }


    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {

        return applicationContext;

    }

    /**
     * 从Spring中获取Bean.
     *
     * @return 如果存在则返回对应的Bean；如果不存在则返回null
     */
    public static Object getBeanNotRequired(String beanName) {
        return getApplicationContext() != null && getApplicationContext().containsBean(beanName) ? getBean(beanName) : null;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        if (getApplicationContext() == null) {
            throw new IllegalStateException("applicaitonContext未注入");
        }
        return getApplicationContext().getBean(name);

    }

    /**
     * 从Spring中获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {

        return getApplicationContext().getBean(clazz);

    }

    /**
     * 从Spring中获取Bean.
     */
    public static <T> Map<String, T> getBeans(Class<T> type) {
        if (getApplicationContext() == null) {
            throw new IllegalStateException("applicaitonContext未注入");
        } else {
            return getApplicationContext().getBeansOfType(type);
        }
    }


    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {

        return getApplicationContext().getBean(name, clazz);

    }

}
