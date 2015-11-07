package io.saso.dash.services.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.saso.dash.server.Client;
import io.saso.dash.services.Service;
import io.saso.dash.services.ServiceScheduler;
import io.saso.dash.util.ThreadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DashServiceScheduler implements ServiceScheduler
{
    private static final Logger logger = LogManager.getLogger();

    private final Service[] services;
    private final ScheduledFuture[] scheduledFutures;

    @Inject
    public DashServiceScheduler(@Named("services") Service[] services)
    {
        this.services = services;
        scheduledFutures = new ScheduledFuture[services.length];
    }

    @Override
    public void schedule(Client client)
    {
        ThreadUtil.THREAD_POOL.submit(() -> {
            for (Service s : services) {
                try {
                    s.start(client);
                }
                catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            for (int i = 0; i < services.length; i++) {
                Service s = services[i];
                final int interval = s.getPollInterval();

                scheduledFutures[i] =
                        ThreadUtil.SCHEDULER.scheduleAtFixedRate(() -> {
                            ThreadUtil.THREAD_POOL.submit(() -> {
                                try {
                                    s.poll(client);
                                }
                                catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                }
                            });
                        }, interval, interval, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void cancel(Client client)
    {
        for (ScheduledFuture f : scheduledFutures) {
            f.cancel(true);
        }

        ThreadUtil.THREAD_POOL.submit(() -> {
            for (Service s : services) {
                try {
                    s.stop(client);
                }
                catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }
}
