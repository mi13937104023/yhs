package com.ddzn.dd.module.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目的启动类
 *
 * @author dd
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ddzn.dd")
@EnableAsync
public class SystemServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SystemServerApplication.class, args);

    }

}
