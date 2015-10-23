package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.entities.*
import io.saso.dash.services.Service
import io.saso.dash.util.Resources

class DashEntityProvider implements EntityProvider
{
    private static final SQL = [
            User         : Resources.get('/sql/user.sql'),
            Provider     : Resources.get('/sql/provider.sql'),
            ProviderUser : Resources.get('/sql/provider_user.sql'),
            AuthToken    : Resources.get('/sql/auth_token.sql')
    ]

    final LiveToken liveToken

    private final EntityFetcher entityManager

    @Inject
    DashEntityProvider(EntityFetcher entityManager,
                       @Assisted LiveToken liveToken)
    {
        this.entityManager = entityManager
        this.liveToken = liveToken
    }

    @Override
    User getUser()
    {
        entityManager.fetch(User, SQL.User, liveToken.userId).get()
    }

    @Override
    Provider getProvider(Service service)
    {
        entityManager.fetch(Provider, SQL.Provider, service).get()
    }

    @Override
    ProviderUser getProviderUser(Service service)
    {
        entityManager.fetch(
                ProviderUser, SQL.ProviderUser, liveToken.userId, service).get()
    }

    @Override
    AuthToken getAuthToken(Service service)
    {
        entityManager.fetch(
                AuthToken, SQL.AuthToken, liveToken.userId, service).get()
    }
}
