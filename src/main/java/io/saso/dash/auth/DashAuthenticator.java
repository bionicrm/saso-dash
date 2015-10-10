package io.saso.dash.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.saso.dash.database.DatabaseExecutor;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class DashAuthenticator implements Authenticator
{
    private final Provider<DatabaseExecutor> dbExecutorProvider;
    private final Provider<LiveToken> liveTokenProvider;

    @Inject
    public DashAuthenticator(Provider<DatabaseExecutor> dbExecutorProvider,
                             Provider<LiveToken> liveTokenProvider)
    {
        this.dbExecutorProvider = dbExecutorProvider;
        this.liveTokenProvider = liveTokenProvider;
    }

    @Override
    public Optional<LiveToken> findLiveToken(String token)
            throws SQLException
    {
        final String sql =
                "SELECT * FROM live_tokens WHERE token = ? LIMIT 1";

        final DatabaseExecutor dbExecutor = dbExecutorProvider.get();
        final Optional<LiveToken> liveToken;

        try (final ResultSet resultSet = dbExecutor.execute(sql, token)) {
            liveToken = createLiveToken(resultSet);
        }

        return liveToken;
    }

    /**
     * Creates a LiveToken from {@code resultSet}. Returns an empty Optional if
     * the LiveToken was invalid or not found.
     *
     * @param resultSet the ResultSet to create the LiveToken from
     *
     * @return an Optional of a LiveToken
     *
     * @see #isLiveTokenValid(LiveToken)
     *
     * @throws SQLException
     */
    private Optional<LiveToken> createLiveToken(ResultSet resultSet)
            throws SQLException
    {
        // if a row exists...
        if (resultSet.next()) {
            final LiveToken liveToken = liveTokenProvider.get();

            // fill liveToken's fields with resultSet
            liveToken.fillFromResultSet(resultSet);

            // check validity of liveToken
            if (isLiveTokenValid(liveToken)) {
                return Optional.of(liveToken);
            }
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
