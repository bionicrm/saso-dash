package io.saso.dash.database.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DashProviderUser implements ProviderUser
{
    private int       id;
    private int       userId;
    private int       providerId;
    private int       authTokenId;
    private String    providerUniqueId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fillFromResultSet(ResultSet resultSet) throws SQLException
    {
        id          = resultSet.getInt("id");
        userId      = resultSet.getInt("user_id");
        providerId  = resultSet.getInt("provider_id");
        authTokenId = resultSet.getInt("auth_token_id");
        providerUniqueId = resultSet.getString("provider_unique_id");
        createdAt   = resultSet.getTimestamp("created_at");
        updatedAt   = resultSet.getTimestamp("updated_at");
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public int getUserId()
    {
        return userId;
    }

    @Override
    public int getProviderId()
    {
        return providerId;
    }

    @Override
    public int getAuthTokenId()
    {
        return authTokenId;
    }

    @Override
    public String getProviderUniqueId()
    {
        return providerUniqueId;
    }

    @Override
    public Timestamp getCreatedAt()
    {
        return createdAt;
    }

    @Override
    public Timestamp getUpdatedAt()
    {
        return updatedAt;
    }

    @Override
    public String getTableName()
    {
        return "provider_users";
    }
}
