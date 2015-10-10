package io.saso.dash.auth;

import io.saso.dash.database.DBEntity;

import java.sql.Timestamp;

/**
 * Represents a LiveToken from the "live_tokens" database.
 */
public interface LiveToken extends DBEntity
{
    /**
     * Gets the ID.
     *
     * @return the ID
     */
    int getId();

    /**
     * Gets the user's ID. The column has a foreign key restraint for a user.
     *
     * @return the user's ID
     */
    int getUserId();

    /**
     * Gets the token. The column has a unique flag. The token is used for
     * validation when the WebSocket client first connects. The client is sent
     * a {@code live_token} cookie that contains this value, and it is up to the
     * server to make sure that the cookie value points to a valid, unexpired
     * LiveToken.
     *
     * @return the token
     */
    String getToken();

    /**
     * Gets the IP of the user that originally requested the token. This should
     * <i>not</i> be used for validation purposes and instead should simply be
     * used as a way to track which IP's requested what token in case any
     * security issues should arise in the future.
     * <p>
     * The returned value is an IPv4 or IPv6 address, followed by a '/' and then
     * the number of bits in the netmask. Example: {@code 10.0.5.7/32}
     *
     * @return the IP
     */
    String getIp();

    /**
     * Gets the expiration Timestamp. This LiveToken should be considered
     * invalid if authentication occurs on or after the returned Timestamp.
     *
     * @return the expiration Timestamp
     */
    Timestamp getExpiresAt();

    /**
     * Gets the created at Timestamp.
     *
     * @return the created at Timestamp
     */
    Timestamp getCreatedAt();

    /**
     * Gets the updated at Timestamp.
     *
     * @return the updated at Timestamp
     */
    Timestamp getUpdatedAt();
}
