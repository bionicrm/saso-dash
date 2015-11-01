package io.saso.dash.server.impl;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.ServerCookieDecoder;
import io.saso.dash.server.CookieFinder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

@Singleton
public class DashCookieFinder implements CookieFinder
{
    @Override
    public Optional<String> find(String name, HttpHeaders headers)
    {
        Iterable<String> headerStrs =
                headers.getAllAndConvert(HttpHeaderNames.COOKIE);

        for (String header : headerStrs) {
            Iterable<Cookie> cookies = ServerCookieDecoder.decode(header);

            for (Cookie cookie : cookies) {
                if (cookie.name().equals(name)) {
                    return Optional.of(urlDecode(cookie.value()));
                }
            }
        }

        return Optional.empty();
    }

    /**
     * URL decodes the given string.
     *
     * @param urlEncoded the URL encoded string
     *
     * @return the URL decoded string
     */
    private String urlDecode(String urlEncoded)
    {
        try {
            return URLDecoder.decode(urlEncoded, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
