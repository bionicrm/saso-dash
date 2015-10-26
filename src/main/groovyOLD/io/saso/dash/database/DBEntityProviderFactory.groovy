package io.saso.dash.database
import io.saso.dash.database.entities.DBLiveToken

interface DBEntityProviderFactory
{
    DBEntityProvider createDBEntityProvider(DBLiveToken liveToken)
}
