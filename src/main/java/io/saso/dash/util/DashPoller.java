package io.saso.dash.util;

import com.google.inject.Singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Singleton
public class DashPoller implements Poller
{
    private final Map<Pollable, TimeInfo> polls = new ConcurrentHashMap<>();

    private final Runnable tracker;

    public DashPoller()
    {
        tracker = new Tracker();

        new Thread(tracker).start();
    }
    
    @Override
    public void registerPollable(Pollable pollable, long interval,
                                 TimeUnit timeUnit)
    {
        polls.put(pollable, new TimeInfo(interval));
    }

    @Override
    public void unregisterPollable(Pollable pollable)
    {
        polls.remove(pollable);
    }

    private final class Tracker implements Runnable
    {
        @Override
        public void run()
        {
            while (true) {
                final long current = System.currentTimeMillis();

                polls.forEach((pollable, timeInfo) -> {
                    final long interval = timeInfo.getInterval();
                    final long last = timeInfo.getLast();
                    final long sinceLast = current - last;

                    if (sinceLast >= interval) {
                        timeInfo.setLast(current);
                        pollable.poll();
                    }
                });
            }
        }
    }

    private final class TimeInfo
    {
        private final long interval;

        private long last;

        public TimeInfo(long interval) {
            this.interval = interval;
        }

        public long getInterval()
        {
            return interval;
        }

        public long getLast()
        {
            return last;
        }

        public void setLast(long last)
        {
            this.last = last;
        }
    }
}
