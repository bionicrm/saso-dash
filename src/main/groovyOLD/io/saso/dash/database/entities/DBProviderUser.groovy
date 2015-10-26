package io.saso.dash.database.entities

import io.saso.dash.database.DBEntityWithTimestamps

interface DBProviderUser extends DBEntityWithTimestamps
{
    int getUserId()

    int getProviderId()

    int getAuthTokenId()

    String getProviderUniqueId()
}
