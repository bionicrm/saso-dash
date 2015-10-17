package io.saso.dash.auth

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.util.Resources
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Singleton
public class DashAuthenticator
@Inject
constructor(val entityManager: EntityManager) : Authenticator
{
    private val liveTokenSql by lazy { Resources.get("/sql/live_token.sql") }

    override fun findLiveToken(token: String): Optional<LiveToken>
    {
        val liveToken = entityManager.execute(
                LiveToken::class, liveTokenSql, arrayListOf(token))

        if (liveToken.isPresent && this isLiveTokenValid liveToken.get()) {
            return liveToken
        }

        return Optional.empty()
    }

    private fun isLiveTokenValid(liveToken: LiveToken) : Boolean =
            liveToken.expiresAt.after(Timestamp.from(Instant.now()))
}
