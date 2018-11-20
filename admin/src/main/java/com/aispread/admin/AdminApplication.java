package com.aispread.admin;

import org.flowable.engine.TaskService;
import org.flowable.spring.boot.ProcessEngineAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication(
        exclude= {
                org.flowable.spring.boot.SecurityAutoConfiguration.class,   //过滤flowable自带的spring security
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,    //过滤flowable自带的spring security
                UserDetailsServiceAutoConfiguration.class,  //过滤flowable自带的spring security
                LiquibaseAutoConfiguration.class,
                ProcessEngineAutoConfiguration.class    //降低flowable自动配置类权重让子类覆盖的方法优先执行
        }

)
@ComponentScan(basePackages = {"com.redimybase", "com.aispread","org.flowable.app"})
@EntityScan(basePackages = {"com.redimybase", "com.aispread"})
@MapperScan({"com.redimybase.manager.*.mapper","com.aispread.manager.*.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("Server started successfully !");
    }


    @RequestMapping("/task")
    String task() {
        System.out.println("################################" + taskService);
        return taskService.toString();
    }


    @Autowired
    private TaskService taskService;
}
