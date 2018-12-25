package com.redimybase.framework.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisMapperRefresh;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * Mybatis--plus配置
 * Created by Irany 2018/6/15 11:38
 */
@Configuration
//@MapperScan({"com.redimybase.manager.*.mapper","com.aispread.manager.*.mapper"})
public class MybatisPlusConfig {

    @Value("${redi.modelerEnable}")
    public boolean modelerEnable;
    /**
     * 分页插件，自动识别数据库类型
     * 多租户，请参考官网【插件扩展】
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean("factoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DruidDataSource dataSource, MybatisConfiguration mybatisConfiguration) throws IOException {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("com.redimybase.manager.*.mapper,com.ainspread.manager.*.mapper");
        factoryBean.setMapperLocations(mapperResources());
        factoryBean.setConfiguration(mybatisConfiguration);
        Properties properties = new Properties();
        properties.put("prefix", "");
        properties.put("blobType", "BLOB");
        factoryBean.setConfigurationProperties(properties);

        //加载mybatis插件
        factoryBean.setPlugins(new Interceptor[]{
                new PaginationInterceptor(),
                new OptimisticLockerInterceptor(),
                new PerformanceInterceptor()
        });

        return factoryBean;
    }

    //@Bean
    private Resource[] mapperResources() throws IOException {
        if (modelerEnable) {
            return ArrayUtils.addAll(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:com/**/mapper/xml/*Mapper.xml"), new PathMatchingResourcePatternResolver().getResources("META-INF/modeler-mybatis-mappings/*.xml"));
        }else{
            return ArrayUtils.addAll(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:com/**/mapper/xml/*Mapper.xml"));
        }
    }

    @Bean
    public MybatisConfiguration mybatisConfiguration() {
        MybatisConfiguration config = new MybatisConfiguration();
        //部分数据库不识别默认的NULL类型（比如oracle，需要配置该属性
        config.setMapUnderscoreToCamelCase(true);
        config.setJdbcTypeForNull(JdbcType.NULL);
        return config;
    }

    /**
     * mybatis xml热加载
     */
    //@Bean
    public MybatisMapperRefresh mybatisMapperRefresh(SqlSessionFactory factoryBean) throws IOException {
        return new MybatisMapperRefresh(mapperResources(), factoryBean, 10, 20, true);
    }


    /**
     * 配置mybatis 扫描mapper接口的路径, 相当于注解@MapperScan，@MapperScan("com.baomidou.mybatisplus.test.h2.entity.mapper")
     */
 /*   @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer config = new MapperScannerConfigurer();
        config.setBasePackage("com.redimybase.manager.*.mapper,com.ainspread.manager.*.mapper");
        return config;
    }*/


    /**
     * 自增配置
     * 0:数据库ID自增,1:用户输入ID
     * 以下2种类型、只有当插入对象ID 为空，才自动填充。
     * 2:ID_WORKER,3:UUID
     * 4:该类型为未设置主键类型,5:字符串全局唯一ID
     * @return
     */
/*    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration();
        conf.setIdType(0);
        conf.setDbColumnUnderline(true);    //是否使用下划线命名
        return conf;
    }*/
   /*
    * oracle数据库配置JdbcTypeForNull
    * 参考：https://gitee.com/baomidou/mybatisplus-boot-starter/issues/IHS8X
    不需要这样配置了，参考 yml:
    mybatis-plus:
      confuguration
        dbc-type-for-null: 'null'
   @Bean
   public ConfigurationCustomizer configurationCustomizer(){
       return new MybatisPlusCustomizers();
   }

   class MybatisPlusCustomizers implements ConfigurationCustomizer {

       @Override
       public void customize(org.apache.ibatis.session.Configuration configuration) {
           configuration.setJdbcTypeForNull(JdbcType.NULL);
       }
   }
   */
}