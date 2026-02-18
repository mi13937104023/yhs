package com.ddzn.dd.framework.common.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 设备数据处理线程池（高并发、频繁任务）
     */
    @Bean(name = "deviceAsyncExecutor")
    public Executor deviceAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(25);                // 核心线程数
        executor.setMaxPoolSize(100);                // 最大线程数
        executor.setQueueCapacity(5000);            // 队列容量
        executor.setKeepAliveSeconds(120);          // 线程空闲回收时间
        executor.setThreadNamePrefix("device-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 队列满时由调用线程执行
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    /**
     * 系统操作日志异步存储线程池（轻量、低频任务）
     */
    @Bean(name = "sysOperateLogExecutor")
    public Executor sysOperateLogExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);                // 核心线程数
        executor.setMaxPoolSize(25);                // 最大线程数
        executor.setQueueCapacity(1500);            // 队列容量
        executor.setKeepAliveSeconds(60);           // 线程空闲回收时间
        executor.setThreadNamePrefix("sys-log-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    /**
     * 物联网卡信息异步处理线程池（每日批量任务）
     */
    @Bean(name = "deviceSimSyncExecutor")
    public Executor deviceSimSyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);                // 核心线程数
        executor.setMaxPoolSize(10);                // 最大线程数
        executor.setQueueCapacity(500);             // 队列容量
        executor.setKeepAliveSeconds(60);           // 线程空闲回收时间
        executor.setThreadNamePrefix("sim-sync-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    /**
     * 全局默认异步线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        return deviceAsyncExecutor();
    }

    /**
     * 异步异常处理
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("异步任务执行失败，方法: {}，参数: {}",
                    method.getName(), Arrays.toString(params), throwable);
        };
    }
}

