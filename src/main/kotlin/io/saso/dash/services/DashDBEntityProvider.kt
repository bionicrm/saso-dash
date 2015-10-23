package io.saso.dash.services

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.DBEntity
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.*
import io.saso.dash.util.Resources
import java.util.*
import kotlin.reflect.KClass

public class DashDBEntityProviderOLD
@Inject
constructor(private val entityManager: EntityManager,
            @Assisted private val liveToken: LiveToken) : DBEntityProvider
{
    private val serviceEntities: MutableMap<String, DBEntity> = hashMapOf()

    override fun liveToken(): LiveToken
    {
        return liveToken
    }

    override fun user() =
            entityManager.executeOrFail(User::class, SQL get User::class,
                    listOf(liveToken.userId))

    override fun provider(service: String) =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(Provider::class,
                        SQL get Provider::class, listOf(service))
            }) as Provider

    override fun providerUser(service: String) =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(ProviderUser::class,
                        SQL get ProviderUser::class,
                        listOf(liveToken.userId, service))
            }) as ProviderUser

    override fun authToken(service: String): AuthToken =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(AuthToken::class,
                        SQL get AuthToken::class,
                        listOf(liveToken.userId, service))
            }) as AuthToken

    private companion object SQL
    {
        val contents by lazy {mapOf(
                User::class         to Resources.get("/sql/user.sql"),
                Provider::class     to Resources.get("/sql/provider.sql"),
                ProviderUser::class to Resources.get("/sql/provider_user.sql"),
                AuthToken::class    to Resources.get("/sql/auth_token.sql")
        )}

        fun get(entity: KClass<*>) =
                contents get entity ?: throw NoSuchElementException()
    }
}
