package io.saso.dash.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadUtil
{
    private static final Logger logger = LogManager.getLogger();

    private ThreadUtil() { }

    public static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool(new DashThreadFactory());

    public static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();

    private static class DashThreadFactory implements ThreadFactory
    {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public DashThreadFactory()
        {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)
                    ? s.getThreadGroup()
                    : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@Nonnull Runnable r)
        {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(), 0);

            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }

            logger.debug("New thread {}, id: {}", t.getName(), t.getId());

            return t;
        }
    }
}
