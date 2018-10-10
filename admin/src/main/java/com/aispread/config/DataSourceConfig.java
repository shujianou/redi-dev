package com.aispread.config;

import com.redimybase.framework.constant.DatasourceEnum;
import com.redimybase.framework.datasource.DruidProperties;
import com.redimybase.framework.datasource.MutiDataSourceProperties;
import com.redimybase.framework.mutidatasource.DynamicDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 数据眼配置
 * Created by Vim 2018/9/14 0:27
 */
@Configuration
public class DataSourceConfig {
    @Autowired
    DruidProperties druidProperties;

    @Autowired
    MutiDataSourceProperties mutiDataSourceProperties;

    /**
     * 另一个数据源
     */
    private DruidDataSource bizDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        mutiDataSourceProperties.config(dataSource);
        return dataSource;
    }

    /**
     * 数据源
     */
    private DruidDataSource dataSources() {
        DruidDataSource dataSource = new DruidDataSource();
        druidProperties.config(dataSource);
        return dataSource;
    }


    /*@Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }*/


    /**
     * 单数据源连接池配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "base", name = "muti-datasource-open", havingValue = "false")
    public DruidDataSource singleDatasource() {
        return dataSources();
    }

    /**
     * 多数据源连接池配置
     */
    @Bean
    @ConditionalOnProperty(prefix = "base", name = "muti-datasource-open", havingValue = "true")
    public DynamicDataSource mutiDataSource() {

        DruidDataSource dataSource = dataSources();
        DruidDataSource bizDataSource = bizDataSource();

        try {
            dataSource.init();
            bizDataSource.init();
        } catch (SQLException sql) {
            sql.printStackTrace();
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> hashMap = new HashMap();
        hashMap.put(DatasourceEnum.DATA_SOURCE_AISPREAD, dataSource);
        dynamicDataSource.setTargetDataSources(hashMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSource);
        return dynamicDataSource;
    }


    /**
     * 数据库选择方言
     * @param druidDataSource
     * @return
     */
 /*   @Bean
    public String hibernateJpaVendorAdapter(DruidDataSource druidDataSource){
        return Hibernates.getDialect(druidDataSource);
    }*/
}
