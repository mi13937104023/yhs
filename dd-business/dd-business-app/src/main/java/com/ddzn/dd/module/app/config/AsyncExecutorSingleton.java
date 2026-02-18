package com.ddzn.dd.module.app.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局异步线程池单例工具类
 */
@Slf4j
public class AsyncExecutorSingleton {

    // 私有构造方法：禁止外部实例化
    private AsyncExecutorSingleton() {
    }

    // 静态内部类：实现延迟加载和线程安全的单例
    private static class Holder {
        // 线程池核心配置
        private static final ExecutorService INSTANCE = new ThreadPoolExecutor(
                5, // 核心线程数（根据CPU核心数和业务调整）
                10, // 最大线程数
                60L, TimeUnit.SECONDS, // 空闲线程存活时间
                new LinkedBlockingQueue<>(100), // 任务队列（避免无界队列导致OOM）
                new ThreadFactory() { // 自定义线程名，便于日志追踪
                    private final AtomicInteger threadNum = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("async-task-" + threadNum.getAndIncrement());
                        thread.setDaemon(false); // 非守护线程（确保任务完成后再退出）
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：让提交任务的线程执行（避免任务丢失）
        );
    }

    // 获取全局唯一线程池实例
    public static ExecutorService getInstance() {
        return Holder.INSTANCE;
    }

    // 优雅关闭线程池（应用退出时调用）
    public static void shutdown() {
        ExecutorService executor = Holder.INSTANCE;
        if (executor.isShutdown()) {
            log.info("线程池已关闭");
            return;
        }

        // 1. 停止接收新任务，等待已提交任务执行
        executor.shutdown();
        try {
            // 2. 等待60秒让任务完成，超时则强制关闭
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // 3. 强制关闭，返回未执行的任务
                List<Runnable> unfinishedTasks = executor.shutdownNow();
                log.warn("线程池强制关闭，未完成任务数：{}", unfinishedTasks.size());
            } else {
                log.info("线程池已优雅关闭");
            }
        } catch (InterruptedException e) {
            // 4. 等待被中断时，强制关闭
            executor.shutdownNow();
            log.warn("线程池关闭被中断", e);
        }
    }
}