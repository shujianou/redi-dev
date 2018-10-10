package com.redimybase.framework.listener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 上下文监听器
 * Created by Irany 2018/8/3 10:00
 */
@Component
public class SpringContextListener implements ApplicationContextAware {

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        if (context == null) {
            throw new IllegalArgumentException("spring context is null");
        }
        return context.getBeansOfType(clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            throw new IllegalArgumentException("spring context is null");
        }
        return context.getBean(clazz);
    }

    public static Object getBean(String name) {
        if (context == null) {
            throw new IllegalArgumentException("spring context is null");
        }
        if (StringUtils.isNotBlank(name) && context.containsBean(name)) {
            return context.getBean(name);
        }
        return null;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        if (context == null) {
            throw new IllegalArgumentException("spring context is null");
        }
        if (StringUtils.isNotBlank(name) && context.containsBean(name)) {
            return context.getBean(name, clazz);
        }
        return null;
    }


    /**
     * 注册监听器Bean
     *
     * @param event
     */
    private static void registerListener(ServletContextEvent event) {
        listeners.forEach(listener -> {
            listener.contextInitialized(event);
        });
    }

    public static void addListener(ServletContextListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }


    /**
     * 释放Spring Data事务资源.
     */
    private void unbindResource() {
        Map<Object, Object> resource = TransactionSynchronizationManager.getResourceMap();

        for (Object key : resource.keySet()) {
            TransactionSynchronizationManager.unbindResourceIfPossible(key);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    /**
     * 待加载的监听器
     */
    private static final List<ServletContextListener> listeners = new ArrayList<>();

    private static ApplicationContext context = null;

    private static Logger log=LoggerFactory.getLogger(SpringContextListener.class);



}
