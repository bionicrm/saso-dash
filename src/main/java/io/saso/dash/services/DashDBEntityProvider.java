package io.saso.dash.services;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.saso.dash.database.DBEntity;
import io.saso.dash.database.EntityManager;
import io.saso.dash.database.EntityManagerOLD;
import io.saso.dash.database.entities.*;
import kotlin.reflect.KClass;

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

        return entityManager.execute(DashUser.class,
                sql, liveToken.getUserId());
    }

    private Provider findProvider(Service service) throws Exception
    {
        final String sql =
                "SELECT * FROM providers WHERE \"name\" = ? LIMIT 1";

        return entityManager.execute(
                DashProvider.class, sql, service.getProviderName());
    }

    private ProviderUser findProviderUser(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM provider_users WHERE user_id = ? AND provider_id = ? LIMIT 1";

        return entityManager.execute(
                DashProviderUser.class, sql, liveToken.getUserId(),
                provider(service).getId());
    }

    private AuthToken findAuthToken(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM auth_tokens WHERE id = ? LIMIT 1";

        return entityManager.execute(
                DashAuthToken.class, sql,
                providerUser(service).getAuthTokenId());
    }
}
