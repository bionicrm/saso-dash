package io.saso.dash.server;

import io.netty.handler.codec.http.HttpHeaders;

import java.util.Optional;

public interface CookieFinder
{
    /**
     * Finds a cookie by name in the given HTTP headers.
     *
     * @param name the name of the cookie
     * @param headers the HTTP headers
     *
     * @return an optional of the cookie value; empty if nonexistent
     */
    Optional<String> find(String name, HttpHeaders headers);
}
