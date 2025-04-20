package com.surge.common.core.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author niewj
 * @date 2022/5/13 20:40
 * @Description:
 */
@Slf4j
@Configuration
public class GlobalThreadPool {
    private static final int coreSizeCpu = Runtime.getRuntime().availableProcessors();
    private static final int keepAliveTime = 60;

    public static final ConcurrentHashMap<Long, Thread> threadMap = new ConcurrentHashMap<>();

    public static final ExecutorService EXECUTOR =  new ThreadPoolExecutor(
            coreSizeCpu, // 核心线程数
            coreSizeCpu, // 最大线程数
            keepAliveTime,      // 线程最大空闲时间
            TimeUnit.SECONDS,   // 时间单位
            new LinkedBlockingQueue<>(2000),    // 任务队列
            new GlobalThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()   // 线程池满，被拒绝执行线程的处理策略
    );


    public static Thread getThread(Long id) {
        return GlobalThreadPool.threadMap.get(id);
    }

    public static Thread setThread(Long id, Thread thread) {
        return GlobalThreadPool.threadMap.put(id, thread);
    }

    public static Thread removeThread(Long id) {
        return GlobalThreadPool.threadMap.remove(id);
    }

    public static void stop(Long id) {
        // 停止任务主线程
        Thread thread = getThread(id);
        if (thread != null) {
            // 强制终止
            thread.stop();
        }
    }

}
