package io.saso.dash.auth

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.database.EntityFetcher
import io.saso.dash.database.entities.LiveToken
import io.saso.dash.util.Resources
import io.saso.dash.util.THREAD_POOL
import java.sql.Timestamp
import java.time.Instant

@Singleton
public class DashAuthenticator
@Inject
constructor(private val entityManager: EntityFetcher) : Authenticator
{
    private val liveTokenSql by lazy { Resources.get("/sql/live_token.sql") }

    override fun authenticate(
            token: String, onAuthentication: (LiveToken) -> Unit,
            onFailure: () -> Unit)
    {
        THREAD_POOL.execute {
            val liveToken = entityManager.fetch(
                    LiveToken::class, liveTokenSql, arrayListOf(token))

            if (liveToken.isPresent && isLiveTokenValid(liveToken.get())) {
                onAuthentication(liveToken.get())
            }
            else {
                onFailure()
            }
        }
    }

    private fun isLiveTokenValid(liveToken: LiveToken) : Boolean =
            liveToken.expiresAt.after(Timestamp.from(Instant.now()))
}
