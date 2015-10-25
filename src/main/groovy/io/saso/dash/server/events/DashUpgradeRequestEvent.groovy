package io.saso.dash.server.events

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import io.netty.handler.codec.http.FullHttpRequest
import io.saso.dash.database.DBEntityProvider

class DashUpgradeRequestEvent implements UpgradeRequestEvent
{
    private final FullHttpRequest request
    private final DBEntityProvider entityProvider

    @Inject
    DashUpgradeRequestEvent(@Assisted FullHttpRequest request,
                            @Assisted DBEntityProvider entityProvider)
    {
        this.request = request
        this.entityProvider = entityProvider
    }

    @Override
    FullHttpRequest getRequest()
    {
        return request
    }

    @Override
    DBEntityProvider getEntityProvider()
    {
        return entityProvider
    }
}
