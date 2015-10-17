package io.saso.dash.auth

import com.google.inject.Inject
import io.saso.dash.database.EntityManager
import io.saso.dash.database.entities.DashLiveToken
import io.saso.dash.database.entities.LiveToken
import java.sql.Timestamp
import java.time.Instant
import java.util.*

public class DashAuthenticator
@Inject constructor(val entityManager: EntityManager) : Authenticator
{
    override fun findLiveToken(token: String): Optional<LiveToken>
    {
        val sql = "SELECT * FROM live_tokens WHERE token = ? LIMIT 1"
        val liveToken = entityManager.execute(LiveToken::class, sql, token)

        if (liveToken.isPresent && isLiveTokenValid(liveToken.get())) {
            return liveToken
        }

        return Optional.empty()
    }

    private fun isLiveTokenValid(liveToken: LiveToken) : Boolean =
            liveToken.expiresAt.after(Timestamp.from(Instant.now()))
}
