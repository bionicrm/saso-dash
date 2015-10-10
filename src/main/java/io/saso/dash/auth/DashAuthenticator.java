package io.saso.dash.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.saso.dash.database.Database;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class DashAuthenticator implements Authenticator
{
    private final Database db;
    private final Provider<LiveToken> liveTokenProvider;

    @Inject
    public DashAuthenticator(Database db, Provider<LiveToken> liveTokenProvider)
    {
        this.db = db;
        this.liveTokenProvider = liveTokenProvider;
    }

    @Override
    public Optional<LiveToken> findLiveToken(String token)
            throws SQLException
    {
        final String sql =
                "SELECT * FROM live_tokens WHERE token = ? LIMIT 1";

        try (Connection conn = db.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            // set params
            statement.setString(1, token);

            return createLiveToken(statement.executeQuery());
        }
    }

    /**
     * Creates a LiveToken from {@code resultSet}. Returns an empty Optional if
     * the LiveToken was invalid or not found.
     *
     * @param resultSet the resultSet to create the LiveToken from
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
