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
    void fillFromResultSet(ResultSet resultSet)
    {
        id               = resultSet.getInt 'id'
        userId           = resultSet.getInt 'user_id'
        providerId       = resultSet.getInt 'provider_id'
        authTokenId      = resultSet.getInt 'auth_token_id'
        providerUniqueId = resultSet.getInt 'provider_unique_id'
        createdAt        = resultSet.getTimestamp 'created_at'
        updatedAt        = resultSet.getTimestamp 'updated_at'
    }
}
