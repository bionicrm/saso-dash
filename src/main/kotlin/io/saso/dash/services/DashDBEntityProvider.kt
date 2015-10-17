package io.saso.dash.services

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.DBEntity
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.*
import io.saso.dash.util.resource
import org.apache.commons.io.IOUtils
import kotlin.properties.Delegates

public class DashDBEntityProvider
@Inject constructor(val entityManager: EntityManager,
                    @Assisted val liveToken: LiveToken) : DBEntityProvider
{
    override val user: User by lazy {
        val sql = resource("/sql/user.sql")

        entityManager.executeOrFail(User::class, sql, liveToken.userId)
    }

    private val serviceEntities: MutableMap<Service, DBEntity> = hashMapOf()

    override fun provider(service: Service) =
            serviceEntities.getOrPut(service, {
                val sql = resource("/sql/provider.sql")

                entityManager.executeOrFail(
                        Provider::class, sql, service.providerName)
            }) as Provider

    override fun providerUser(service: Service) =
            serviceEntities.getOrPut(service, {
                val sql = resource("/sql/provider_user.sql")

                entityManager.executeOrFail(ProviderUser::class, sql,
                        liveToken.userId, service.providerName)
            }) as ProviderUser

    override fun authToken(service: Service): AuthToken =
            serviceEntities.getOrPut(service, {
                val sql = resource("/sql/auth_token.sql")

                entityManager.executeOrFail(AuthToken::class, sql,
                        liveToken.userId, service.providerName)
            }) as AuthToken
}
