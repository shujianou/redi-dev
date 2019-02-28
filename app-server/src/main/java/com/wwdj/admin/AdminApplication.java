package com.wwdj.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(
        exclude= {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,    //过滤flowable自带的spring security
                UserDetailsServiceAutoConfiguration.class,  //过滤flowable自带的spring security
                LiquibaseAutoConfiguration.class,
        }

)
@ComponentScan(basePackages = {"com.redimybase","com.wwdj"})
@EntityScan(basePackages = {"com.redimybase", "com.wwdj"})
@MapperScan({"com.redimybase.manager.*.mapper","com.wwdj.manager.*.mapper"})
@EnableSwagger2
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("Server started successfully !");
    }

}