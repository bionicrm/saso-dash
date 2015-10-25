package io.saso.dash.server.events

import io.netty.handler.codec.http.FullHttpRequest
import io.saso.dash.database.DBEntityProvider

interface UpgradeRequestEvent
{
    FullHttpRequest getRequest()

    DBEntityProvider getEntityProvider()
}
