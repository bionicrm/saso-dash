package io.saso.dash.server.events

import io.netty.handler.codec.http.FullHttpRequest
import io.saso.dash.database.DBEntityProvider

interface ServerEventsFactory
{
    UpgradeRequestEvent createUpgradeRequestEvent(
            FullHttpRequest request, DBEntityProvider entityProvider)
}
