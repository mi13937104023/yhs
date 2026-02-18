package com.ddzn.dd.module.app.config;

import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

/**
 * 利用Spring生命周期自动关闭线程池
 */
@Component // 被Spring管理，随容器启动/销毁
public class AsyncThreadPoolShutdownHandler {

    // Spring容器销毁前自动调用（应用关闭时）
    @PreDestroy
    public void onDestroy() {
        AsyncExecutorSingleton.shutdown();
    }
}