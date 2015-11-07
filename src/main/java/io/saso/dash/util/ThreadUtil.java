package io.saso.dash.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class ThreadUtil
{
    private ThreadUtil() { }

    public static final ExecutorService THREAD_POOL =
            Executors.newCachedThreadPool();

    public static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();
}
