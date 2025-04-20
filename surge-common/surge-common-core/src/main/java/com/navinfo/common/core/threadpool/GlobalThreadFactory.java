package com.surge.common.core.threadpool;

import com.surge.common.core.utils.StringUtils;

import java.util.concurrent.ThreadFactory;

public class GlobalThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        if(StringUtils.isEmpty(thread.getName())) {
         thread.setName("global-thread");
        }
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (Thread.NORM_PRIORITY != thread.getPriority()) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
