package com.redimybase.flowable.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.redimybase.flowable.parser.factory.ConfigTaskUserActivityBehaviorFactory;
import org.flowable.app.properties.FlowableModelerAppProperties;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * flowable配置
 * Created by Vim 2018/10/8 15:32
 */
@Configuration
public class FlowableConfig {

    @Bean
    public StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration(DruidDataSource dataSources,ConfigTaskUserActivityBehaviorFactory configTaskUserActivityBehaviorFactory) {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setActivityBehaviorFactory(configTaskUserActivityBehaviorFactory);
        configuration.setDataSource(dataSources);
        configuration.setDatabaseType("mysql");
        configuration.setDatabaseSchemaUpdate("true");
        return configuration;
    }

    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        return new FlowableModelerAppProperties();
    }


    @Bean
    public ConfigTaskUserActivityBehaviorFactory configTaskUserActivityBehaviorFactory() {
        return new ConfigTaskUserActivityBehaviorFactory();
    }
}
