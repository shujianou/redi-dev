package com.redimybase.flowable.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.redimybase.flowable.parser.factory.ConfigTaskUserActivityBehaviorFactory;
import org.flowable.app.properties.FlowableModelerAppProperties;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.FlowableMailProperties;
import org.flowable.spring.boot.FlowableProperties;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.flowable.spring.boot.process.Process;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * flowable配置
 * Created by Vim 2018/10/8 15:32
 *
 * @author Vim
 */
@Configuration
@EnableConfigurationProperties(FlowableProperties.class)
public class FlowableConfig extends ProcessEngineAutoConfiguration {

 /*   @Bean
    public StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration(DruidDataSource dataSources, ConfigTaskUserActivityBehaviorFactory configTaskUserActivityBehaviorFactory) {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setActivityBehaviorFactory(configTaskUserActivityBehaviorFactory);
        configuration.setDataSource(dataSources);
        configuration.setDatabaseType("mysql");
        configuration.setDatabaseSchemaUpdate("true");
        return configuration;
    }
*/

    public FlowableConfig(FlowableProperties flowableProperties,
                          FlowableProcessProperties processProperties, FlowableIdmProperties idmProperties,
                          FlowableMailProperties mailProperties) {
        super(flowableProperties, processProperties, idmProperties, mailProperties);
    }
    @Override
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager,
                                                                             @Process ObjectProvider<AsyncExecutor> asyncExecutorProvider) throws IOException {

        return super.springProcessEngineConfiguration(dataSource, platformTransactionManager, asyncExecutorProvider);
    }

    @Bean
    @Override
    public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration configuration) throws Exception {
        //注入配置用户行为工厂配置
        configuration.setActivityBehaviorFactory(configTaskUserActivityBehaviorFactory());
        return super.processEngine(configuration);
    }

    @Bean
    public FlowableModelerAppProperties flowableModelerAppProperties() {
        return new FlowableModelerAppProperties();
    }


    public ConfigTaskUserActivityBehaviorFactory configTaskUserActivityBehaviorFactory() {
        return new ConfigTaskUserActivityBehaviorFactory();
    }
}
