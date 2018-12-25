package com.redimybase.module.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.redimybase", "com.aispread"})
@EntityScan(basePackages = {"com.redimybase.manager", "com.aispread.manager"})
@MapperScan({"com.redimybase.manager.*.mapper","com.aispread.manager.*.mapper"})
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }
}
