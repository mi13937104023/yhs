package com.ddzn.dd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.ddzn.dd")
public class GatewayServerApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(GatewayServerApplication.class, args);
    }

}
