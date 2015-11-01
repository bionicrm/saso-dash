package io.saso.dash.database
import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.entities.*
import io.saso.dash.services.Service
import io.saso.dash.util.Resources

class DashDBEntityProvider implements DBEntityProvider
{
    private static final SQL = [
            User         : Resources.get('/sql/user.sql'),
            Provider     : Resources.get('/sql/find_provider.sql'),
            ProviderUser : Resources.get('/sql/provider_user.sql'),
            AuthToken    : Resources.get('/sql/find_auth_token.sql')
    ]

    final DBLiveToken liveToken

    private final DBFetcher entityFetcher

    @Inject
    DashDBEntityProvider(DBFetcher entityFetcher,
                         @Assisted DBLiveToken liveToken)
    {
        this.entityFetcher = entityFetcher
        this.liveToken = liveToken
    }

    @Override
    DBUser getUser()
    {
        return entityFetcher.fetch(DBUser, SQL.User, liveToken.userId).get()
    }

    @Override
    DBService getProvider(Service service)
    {
        return entityFetcher.fetch(DBService, SQL.Provider, service.name).get()
    }

    @Override
    DBServiceUser getProviderUser(Service service)
    {
        return entityFetcher.fetch(
                DBServiceUser, SQL.ProviderUser, liveToken.userId,
                service.name).get()
    }

    @Override
    DBAuthToken getAuthToken(Service service)
    {
        return entityFetcher.fetch(
                DBAuthToken, SQL.AuthToken, liveToken.userId, service.name)
                .get()
    }
}
