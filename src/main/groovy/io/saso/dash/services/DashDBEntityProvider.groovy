package io.saso.dash.services

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.AuthToken
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.database.entities.Provider
import io.saso.dash.database.entities.ProviderUser
import io.saso.dash.database.entities.User
import io.saso.dash.util.Resources

class DashDBEntityProvider implements DBEntityProvider
{
    private static final SQL = [
            User         : Resources.get('/sql/user.sql'),
            Provider     : Resources.get('/sql/provider.sql'),
            ProviderUser : Resources.get('/sql/provider_user.sql'),
            AuthToken    : Resources.get('/sql/auth_token.sql')
    ]

    final LiveToken liveToken

    private final EntityManager entityManager
    private final Service service

    @Inject
    DashDBEntityProvider(EntityManager entityManager,
                         @Assisted LiveToken liveToken,
                         @Assisted Service service)
    {
        this.entityManager = entityManager
        this.liveToken = liveToken
        this.service = service
    }

    @Override
    User getUser()
    {
        entityManager.execute(User, SQL.User, liveToken.userId).get()
    }

    @Override
    Provider getProvider()
    {
        entityManager.execute(Provider, SQL.Provider, service).get()
    }

    @Override
    ProviderUser getProviderUser()
    {
        entityManager.execute(
                ProviderUser, SQL.ProviderUser, liveToken.userId, service).get()
    }

    @Override
    AuthToken getAuthToken()
    {
        entityManager.execute(
                AuthToken, SQL.AuthToken, liveToken.userId, service).get()
    }
}
