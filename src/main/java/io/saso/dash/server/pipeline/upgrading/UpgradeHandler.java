package io.saso.dash.server.pipeline.upgrading;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.saso.dash.database.entities.DBLiveToken;

public interface UpgradeHandler
{
    void upgrade(ChannelHandlerContext ctx, FullHttpRequest req,
                 DBLiveToken liveToken);

    void setNext(UpgradeHandler next);
}
