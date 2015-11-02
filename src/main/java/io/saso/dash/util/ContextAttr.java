package io.saso.dash.util;

import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.AttributeKey;
import io.saso.dash.database.entities.DBLiveToken;

public final class ContextAttr
{
    private ContextAttr() { }

    public static final AttributeKey<DBLiveToken> LIVE_TOKEN =
            AttributeKey.valueOf("live token");

    public static final AttributeKey<String> TOKEN_COOKIE_VALUE =
            AttributeKey.valueOf("token cookie value");

    public static final AttributeKey<WebSocketServerHandshaker> WS_HANDSHAKER =
            AttributeKey.valueOf("ws handshaker");
}
