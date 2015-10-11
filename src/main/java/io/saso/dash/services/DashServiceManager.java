package io.saso.dash.services;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.database.EntityManager;
import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.DashAuthToken;
import io.saso.dash.database.entities.DashProvider;
import io.saso.dash.database.entities.DashProviderUser;
import io.saso.dash.database.entities.DashUser;
import io.saso.dash.database.entities.LiveToken;
import io.saso.dash.database.entities.Provider;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;
import io.saso.dash.redis.Redis;
import io.saso.dash.redis.tables.RedisConnections;
import io.saso.dash.util.LoggingUtil;

import java.util.*;
import java.util.concurrent.*;

public class DashServiceManager implements ServiceManager
{
    private static final ExecutorService CACHED_THREAD_POOL =
            Executors.newCachedThreadPool();
    private static final ScheduledExecutorService SCHEDULED_EXECUTOR =
            Executors.newSingleThreadScheduledExecutor();

    private final RedisConnections redisConnections;
    private final EntityManager entityManager;
    private final List<Service> services = new ArrayList<>();
    private final Map<Service, ScheduledFuture> serviceSchedules =
            new HashMap<>();

    private ChannelHandlerContext ctx;
    private LiveToken liveToken;

    @Inject
    public DashServiceManager(/* TODO: use preferences */
                              RedisConnections redisConnections,
                              EntityManager entityManager,
                              @Named("github") Service githubService,
                              @Named("google") Service googleService)
    {
        this.redisConnections = redisConnections;
        this.entityManager = entityManager;
        services.add(githubService);
        services.add(googleService);
    }

    @Override
    public void start() throws Exception
    {
        final User user = getUser();

        services.forEach(service -> {
            // inject service (context)
            service.setContext(ctx);

            // execute service start
            CACHED_THREAD_POOL.execute(() -> executeChecked(() -> {
                final Provider provider = getProvider(service);
                final ProviderUser providerUser = getProviderUser(provider);
                final AuthToken authToken = getAuthToken(providerUser);

                // inject service (entities)
                service.setUser(user);
                service.setProviderUser(providerUser);
                service.setAuthToken(authToken);

                service.start();
            }));

            // poll service from thread pool at fixed rate
            serviceSchedules.put(service,
                    SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                            () -> CACHED_THREAD_POOL.execute(
                                    () -> executeChecked(service::poll)),
                            service.getPollInterval(),
                            service.getPollInterval(), TimeUnit.SECONDS));
        });
    }

    @Override
    public void stop()
    {
        CACHED_THREAD_POOL.execute(() -> {
            services.forEach(service -> {
                // cancel service schedule
                serviceSchedules.get(service).cancel(true);

                // execute service stops
                executeChecked(service::stop);
            });

            // remove connection for user
            redisConnections.remove(liveToken.getUserId());
        });
    }

    @Override
    public void setContext(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public void setLiveToken(LiveToken liveToken)
    {
        this.liveToken = liveToken;
    }

    private User getUser() throws Exception
    {
        final String sql = "SELECT * FROM users WHERE id = ? LIMIT 1";

        final Optional<User> user = entityManager.execute(DashUser.class, sql,
                liveToken.getUserId());

        return user.get();
    }

    private Provider getProvider(Service service) throws Exception
    {
        final String sql = "SELECT * FROM providers WHERE \"name\" = ? LIMIT 1";

        final Optional<Provider> providerUser = entityManager.execute(
                DashProvider.class, sql, service.getProviderName());

        return providerUser.get();
    }

    private ProviderUser getProviderUser(Provider provider) throws Exception
    {
        final String sql = "SELECT * FROM provider_users WHERE user_id = ? AND provider_id = ? LIMIT 1";

        final Optional<ProviderUser> providerUser = entityManager.execute(
                DashProviderUser.class, sql, liveToken.getUserId(),
                provider.getId());

        return providerUser.get();
    }

    private AuthToken getAuthToken(ProviderUser providerUser) throws Exception
    {
        final String sql = "SELECT * FROM auth_tokens WHERE id = ? LIMIT 1";

        final Optional<AuthToken> authToken = entityManager.execute(
                DashAuthToken.class, sql, providerUser.getAuthTokenId());

        return authToken.get();
    }

    private void executeChecked(RunnableThrowable runnableThrowable)
    {
        try {
            runnableThrowable.run();
        }
        catch (Exception e) {
            LoggingUtil.logThrowable(e, getClass());
        }
    }

    private interface RunnableThrowable
    {
        void run() throws Exception;
    }
}
