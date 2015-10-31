package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps
import io.saso.dash.database.DBTimestampable

interface DBProviderUser extends DBTimestampable
{
    int getUserId()

    int getProviderId()

    int getAuthTokenId()

    String getProviderUniqueId()
}
