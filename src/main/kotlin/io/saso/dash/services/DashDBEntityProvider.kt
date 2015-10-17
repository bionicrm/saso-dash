package io.saso.dash.services

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.DBEntity
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.*
import io.saso.dash.util.Resources

public class DashDBEntityProvider
@Inject constructor(val entityManager: EntityManager,
                    @Assisted val liveToken: LiveToken) : DBEntityProvider
{
    private val serviceEntities: MutableMap<Service, DBEntity> = hashMapOf()

    private val sql = mapOf(
            User::class         to Resources.get("/sql/user.sql"),
            Provider::class     to Resources.get("/sql/provider.sql"),
            ProviderUser::class to Resources.get("/sql/provider_user.sql"),
            AuthToken::class    to Resources.get("/sql/auth_token.sql")
    )

    override fun user(): User
    {
        val sql = sql.getOrImplicitDefault(User::class)

        return entityManager.executeOrFail(User::class, sql, liveToken.userId)
    }

    override fun provider(service: Service) =
            serviceEntities.getOrPut(service, {
                val sql = sql.getOrImplicitDefault(Provider::class)

                entityManager.executeOrFail(
                        Provider::class, sql, service.providerName)
            }) as Provider

    override fun providerUser(service: Service) =
            serviceEntities.getOrPut(service, {
                val sql = sql.getOrImplicitDefault(ProviderUser::class)

                entityManager.executeOrFail(ProviderUser::class, sql,
                        liveToken.userId, service.providerName)
            }) as ProviderUser

    override fun authToken(service: Service): AuthToken =
            serviceEntities.getOrPut(service, {
                val sql = sql.getOrImplicitDefault(AuthToken::class)

                entityManager.executeOrFail(AuthToken::class, sql,
                        liveToken.userId, service.providerName)
            }) as AuthToken
}
