package io.saso.dash.auth

import com.google.inject.Inject
import com.google.inject.Singleton
import io.saso.dash.database.DBEntityFetcher
import io.saso.dash.database.entities.DBLiveToken
import io.saso.dash.util.Resources
import io.saso.dash.util.THREAD_POOL
import java.sql.Timestamp
import java.time.Instant

@Singleton
public class DashAuthenticator
@Inject
constructor(private val entityManager: DBEntityFetcher) : Authenticator
{
    private val liveTokenSql by lazy { Resources.get("/sql/live_token.sql") }

    override fun authenticate(
            token: String, onAuthentication: (DBLiveToken) -> Unit,
            onFailure: () -> Unit)
    {
        THREAD_POOL.execute {
            val liveToken = entityManager.fetch(
                    DBLiveToken::class, liveTokenSql, arrayListOf(token))

            if (liveToken.isPresent && isLiveTokenValid(liveToken.get())) {
                onAuthentication(liveToken.get())
            }
            else {
                onFailure()
            }
        }
    }

    private fun isLiveTokenValid(liveToken: DBLiveToken) : Boolean =
            liveToken.expiresAt.after(Timestamp.from(Instant.now()))
}
