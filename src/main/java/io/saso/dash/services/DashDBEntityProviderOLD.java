package io.saso.dash.services;

import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.Provider;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;

public class DashDBEntityProviderOLD implements DBEntityProviderOLD
{
    @Override
    public User user() throws Exception
    {
        return null;
    }

    @Override
    public Provider provider(Service service) throws Exception
    {
        return null;
    }

    @Override
    public ProviderUser providerUser(Service service) throws Exception
    {
        return null;
    }

    @Override
    public AuthToken authToken(Service service) throws Exception
    {
        return null;
    }
/*    private final EntityManager entityManager;
    private final LiveToken liveToken;
    private final Map<Service, DBEntity> entities = new HashMap<>();

    private User user;

    @Inject
    public DashDBEntityProviderOLD(EntityManager entityManager,
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

        return entityManager.execute(User.class, sql, liveToken.getUserId());
    }

    private Provider findProvider(Service service) throws Exception
    {
        final String sql =
                "SELECT * FROM providers WHERE \"name\" = ? LIMIT 1";

        return entityManager.execute(
                Provider.class, sql, service.getProviderName());
    }

    private ProviderUser findProviderUser(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM provider_users WHERE user_id = ? AND provider_id = (SELECT id FROM providers WHERE \"name\" = ?) LIMIT 1";

        return entityManager.execute(
                ProviderUser.class, sql, liveToken.getUserId(), service.getProviderName());
    }

    private AuthToken findAuthToken(Service service)
            throws Exception
    {
        final String sql =
                "SELECT * FROM auth_tokens WHERE id = (SELECT auth_token_id FROM provider_users WHERE user_id = ? AND provider_id = (SELECT id FROM providers WHERE \"name\" = ?)) LIMIT 1";

        return entityManager.execute(
                AuthToken.class, sql, liveToken.getUserId(), service.getProviderName());
    }*/
}
