package io.saso.dash.database
import com.google.inject.Inject
import io.saso.dash.database.entities.*
import io.saso.dash.services.Service
import io.saso.dash.util.Resources

class DashDBEntityProvider implements DBEntityProvider
{
    private static final SQL = [
            User         : Resources.get('/sql/user.sql'),
            Provider     : Resources.get('/sql/provider.sql'),
            ProviderUser : Resources.get('/sql/provider_user.sql'),
            AuthToken    : Resources.get('/sql/auth_token.sql')
    ]

    final DBLiveToken liveToken

    private final DBEntityFetcher entityFetcher

    @Inject
    DashDBEntityProvider(DBEntityFetcher entityFetcher, DBLiveToken liveToken)
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
    DBProvider getProvider(Service service)
    {
        return entityFetcher.fetch(DBProvider, SQL.Provider, service.name).get()
    }

    @Override
    DBProviderUser getProviderUser(Service service)
    {
        return entityFetcher.fetch(
                DBProviderUser, SQL.ProviderUser, liveToken.userId,
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
