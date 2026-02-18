package com.ddzn.dd.module.app;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目的启动类
 *
 * @author dd
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ddzn.dd")
@EnableAsync
@EnableRabbit
@EnableScheduling
public class BusinessServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(BusinessServerApplication.class, args);

    }

}
