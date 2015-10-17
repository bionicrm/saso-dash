package io.saso.dash.auth

import com.google.inject.Inject
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.DashLiveToken
import io.saso.dash.database.entities.LiveToken
import java.sql.Timestamp
import java.time.Instant

public class DashAuthenticator
@Inject constructor(val entityManager: EntityManager) : Authenticator
{
    override fun findLiveToken(token: String): LiveToken?
    {
        val sql = "SELECT * FROM live_tokens WHERE token = ? LIMIT 1"

        val liveToken: LiveToken? =
                entityManager.execute(DashLiveToken::class, sql)

        if (liveToken == null || ! isLiveTokenValid(liveToken)) {
            return null
        }

        return liveToken
    }

    private fun isLiveTokenValid(liveToken: LiveToken) : Boolean =
            liveToken.expiresAt.after(Timestamp.from(Instant.now()))
}
