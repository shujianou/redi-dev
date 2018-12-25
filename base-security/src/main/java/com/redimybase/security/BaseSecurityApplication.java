package com.redimybase.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients({"com.aispread.service.api.security"})
@ComponentScan(basePackages = {"com.redimybase", "com.aispread"})
@EntityScan(basePackages = {"com.redimybase.manager", "com.aispread.manager"})
@MapperScan({"com.redimybase.manager.*.mapper","com.aispread.manager.*.mapper"})
public class BaseSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseSecurityApplication.class, args);
    }
}
