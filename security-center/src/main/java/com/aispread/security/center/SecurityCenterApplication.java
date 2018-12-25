package com.aispread.security.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "com.aispread.security.center",//鉴权中心包
        "com.redimybase.framework.aop",//框架AOP
        "com.aispread.service.api.security"//API服务接口包
})
@EnableFeignClients(basePackages = {"com.aispread.service.api.security"})
@EnableDiscoveryClient
@SpringBootApplication
public class SecurityCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityCenterApplication.class, args);
    }
}
