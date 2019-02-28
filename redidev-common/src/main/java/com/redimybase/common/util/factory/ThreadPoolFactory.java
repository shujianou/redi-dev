package com.redimybase.common.util.factory;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/2 12:22
 */
public class ThreadPoolFactory {


    private static final String DEFAULT_NAME = "lily";

    private static final int LIVE_TIME = 200;

    public static ExecutorService create() {
        int maxCount = Runtime.getRuntime().availableProcessors() >> 1;
        return create(maxCount, maxCount, DEFAULT_NAME, 1000);
    }

    public static ExecutorService create(int coreThreadCount) {
        int maxCount = Runtime.getRuntime().availableProcessors() >> 1;
        coreThreadCount = coreThreadCount <= 0 ? 1 : (coreThreadCount > maxCount ? maxCount : coreThreadCount);
        return create(coreThreadCount, maxCount, DEFAULT_NAME, 1000);
    }

    /**
     * 创建一个线程池
     *
     * @param coreThreadCount 核心线程数量
     * @param maxThreadCount 最大线程数量
     * @param name 线程名称
     * @param queueMaximum 缓冲队列大小
     * @return java.util.concurrent.Executor
     * @author Charlie
     * @date 2018/11/2 13:06
     */
    public static ExecutorService create(int coreThreadCount, int maxThreadCount, String name, Integer queueMaximum) {
        //创建线程池
        return new ThreadPoolExecutor(
                coreThreadCount,
                maxThreadCount,
                LIVE_TIME,
                TimeUnit.MICROSECONDS,
                queueMaximum == null ? new LinkedBlockingQueue<>() : new LinkedBlockingQueue<>(queueMaximum),
                new MyThreadFactory(name),
                new DealRejectHandler()
        ){
            @Override
            public void shutdown() {
                if (! isShutdown()) {
                    super.shutdown();
                }
            }
        };
    }


    private static class MyThreadFactory implements ThreadFactory {

        private ThreadGroup group;
        private StringBuilder threadName;
        private AtomicLong threadIndex = new AtomicLong(0);

        {
            SecurityManager manager = System.getSecurityManager();
            group = manager == null ? Thread.currentThread().getThreadGroup() : manager.getThreadGroup();
        }


        MyThreadFactory(String threadName) {
            this.threadName = new StringBuilder(threadName);
        }

        /**
         * 创建一个线程
         *
         * @param r r
         * @return java.lang.Thread
         * @author Charlie
         * @date 2018/11/2 13:00
         */
        @Override
        public Thread newThread(@NonNull Runnable r) {
            String name = threadName.append("-").append(threadIndex.incrementAndGet()).toString();
            Thread thread = new Thread(group, r, name);
            if (thread.isDaemon()) {
                thread.setDaemon(Boolean.FALSE);
            }
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }


    private static class DealRejectHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //被拒绝的线程,手动执行
            if (r != null && ! executor.isShutdown()) {
                r.run();
            }
        }
    }


}
