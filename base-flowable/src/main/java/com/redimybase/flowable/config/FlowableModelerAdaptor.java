package com.redimybase.flowable.config;

import com.redimybase.flowable.interceptor.FlowableModelerUserHandleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Vim 2018/10/11 9:20
 */
@Configuration
public class FlowableModelerAdaptor extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new FlowableModelerUserHandleInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/logout","toLogin","/error/**");
        super.addInterceptors(registry);
    }
}
