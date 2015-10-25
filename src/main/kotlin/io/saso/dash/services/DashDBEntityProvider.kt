package io.saso.dash.services

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.saso.dash.database.DBEntityProvider
import io.saso.dash.database.DBEntityFetcher
import io.saso.dash.database.entities.*
import io.saso.dash.util.Resources
import java.util.*
import kotlin.reflect.KClass

public class DashDBEntityProviderOLD
@Inject
constructor(private val entityManager: DBEntityFetcher,
            @Assisted private val liveToken: DBLiveToken) : DBEntityProvider
{
    private val serviceEntities: MutableMap<String, DBEntity> = hashMapOf()

    override fun liveToken(): DBLiveToken
    {
        return liveToken
    }

    override fun user() =
            entityManager.executeOrFail(DBUser::class, SQL get DBUser::class,
                    listOf(liveToken.userId))

    override fun provider(service: String) =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(DBProvider::class,
                        SQL get DBProvider::class, listOf(service))
            }) as DBProvider

    override fun providerUser(service: String) =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(DBProviderUser::class,
                        SQL get DBProviderUser::class,
                        listOf(liveToken.userId, service))
            }) as DBProviderUser

    override fun authToken(service: String): DBAuthToken =
            serviceEntities.getOrPut(service, {
                entityManager.executeOrFail(DBAuthToken::class,
                        SQL get DBAuthToken::class,
                        listOf(liveToken.userId, service))
            }) as DBAuthToken

    private companion object SQL
    {
        val contents by lazy {mapOf(
                DBUser::class         to Resources.get("/sql/user.sql"),
                DBProvider::class     to Resources.get("/sql/provider.sql"),
                DBProviderUser::class to Resources.get("/sql/provider_user.sql"),
                DBAuthToken::class    to Resources.get("/sql/auth_token.sql")
        )}

        fun get(entity: KClass<*>) =
                contents get entity ?: throw NoSuchElementException()
    }
}
