package io.saso.dash.auth;

public interface Authenticator
{
    boolean isTokenValid(String token);
}
