package io.saso.dash.services;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.EntityManager;
import io.saso.dash.database.entities.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DashDBEntityProvider implements DBEntityProvider
{
    private final EntityManager entityManager;
    private final LiveToken liveToken;
    private final Map<Service, DBEntity> entities = new HashMap<>();

    private User user;

    @Inject
    public DashDBEntityProvider(EntityManager entityManager,
                                @Assisted LiveToken liveToken)
    {
        this.entityManager = entityManager;
        this.liveToken = liveToken;
    }

    @Override
    public LiveToken liveToken()
    {
        return liveToken;
    }

    @Override
    public synchronized User user() throws Exception
    {
        if (user == null) {
            user = findUser();
        }

        return user;
    }

    @Override
    public synchronized Provider provider(Service service) throws Exception
    {
        DBEntity entity = entities.get(service);

        if (entity == null) {
            entity = findProvider(service);

            entities.put(service, entity);
        }

        return (Provider) entity;
    }

    @Override
    public synchronized ProviderUser providerUser(Service service)
            throws Exception
    {
        DBEntity entity = entities.get(service);

        if (entity == null) {
            entity = findProviderUser(service);

            entities.put(service, entity);
        }

        return (ProviderUser) entity;
    }

    @Override
    public synchronized AuthToken authToken(Service service)
            throws Exception
    {
        DBEntity entity = entities.get(service);

        if (entity == null) {
            entity = findAuthToken(service);

            entities.put(service, entity);
        }

        return (AuthToken) entity;
    }

    private User findUser() throws Exception
    {
        final String sql = "SELECT * FROM users WHERE id = ? LIMIT 1";

        final Optional<User> user = entityManager.execute(DashUser.class,
                sql, liveToken().getUserId());

        return user.get();
    }

    private Provider findProvider(Service service) throws Exception
    {
        final String sql =
                "SELECT * FROM providers WHERE \"name\" = ? LIMIT 1";

        final Optional<Provider> providerUser = entityManager.execute(
                DashProvider.class, sql, service.getProviderName());

        return providerUser.get();
    }

    private ProviderUser findProviderUser(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM provider_users WHERE user_id = ? AND provider_id = ? LIMIT 1";

        final Optional<ProviderUser> providerUser = entityManager.execute(
                DashProviderUser.class, sql, liveToken().getUserId(),
                provider(service).getId());

        return providerUser.get();
    }

    private AuthToken findAuthToken(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM auth_tokens WHERE id = ? LIMIT 1";

        final Optional<AuthToken> authToken = entityManager.execute(
                DashAuthToken.class, sql,
                providerUser(service).getAuthTokenId());

        return authToken.get();
    }
}
