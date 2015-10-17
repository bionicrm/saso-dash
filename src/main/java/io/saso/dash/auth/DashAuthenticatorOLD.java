package io.saso.dash.auth;

import com.google.inject.Inject;
import io.saso.dash.database.EntityManagerOLD;
import io.saso.dash.database.entities.DashLiveToken;
import io.saso.dash.database.entities.LiveToken;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class DashAuthenticatorOLD implements AuthenticatorOLD
{
    private final EntityManagerOLD entityManager;

    @Inject
    public DashAuthenticatorOLD(EntityManagerOLD entityManager)
    {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<LiveToken> findLiveToken(String token)
            throws Exception
    {
        final String sql = "SELECT * FROM live_tokens WHERE token = ? LIMIT 1";

        final Optional<LiveToken> liveToken =
                entityManager.execute(DashLiveToken.class, sql, token);

        if (liveToken.isPresent() && isLiveTokenValid(liveToken.get())) {
            return liveToken;
        }

        return Optional.empty();
    }

    /**
     * Gets if {@code liveToken} is valid. That is, it has not expired.
     *
     * @param liveToken the LiveToken to validate
     *
     * @return if {@code liveToken} is valid
     */
    private boolean isLiveTokenValid(LiveToken liveToken)
    {
        return liveToken.getExpiresAt().after(Timestamp.from(Instant.now()));
    }
}
