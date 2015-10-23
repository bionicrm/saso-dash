package io.saso.dash.database.entities

import java.sql.ResultSet
import java.sql.Timestamp

class DashProviderUser implements ProviderUser
{
    final String tableName = 'provider_users'

    int       id
    int       userId
    int       providerId
    int       authTokenId
    String    providerUniqueId
    Timestamp createdAt
    Timestamp updatedAt

    @Override
    void fill(ResultSet results)
    {
        id               = results.getInt 'id'
        userId           = results.getInt 'user_id'
        providerId       = results.getInt 'provider_id'
        authTokenId      = results.getInt 'auth_token_id'
        providerUniqueId = results.getInt 'provider_unique_id'
        createdAt        = results.getTimestamp 'created_at'
        updatedAt        = results.getTimestamp 'updated_at'
    }
}
