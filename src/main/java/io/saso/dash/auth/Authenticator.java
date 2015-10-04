package io.saso.dash.auth;

import java.util.Optional;

public interface Authenticator
{
    Optional<LiveToken> getLiveToken(String token);
}
