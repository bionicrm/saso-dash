package io.saso.dash.database
import io.saso.dash.database.entities.LiveToken

interface EntityProviderFactory
{
    EntityProvider createDBEntityProvider(LiveToken liveToken)
}
