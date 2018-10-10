package com.aispread.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.redimybase", "com.aispread"})
@EntityScan(basePackages = {"com.redimybase", "com.aispread"})
@MapperScan({"com.redimybase.manager.*.mapper","com.aispread.manager.*.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("Server started successfully !");
    }
}
