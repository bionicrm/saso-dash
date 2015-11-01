package io.saso.dash.database.entities.impl;

import io.saso.dash.database.entities.DBServiceUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DashDBServiceUser implements DBServiceUser
{
    private int id;
    private int userId;
    private int serviceId;
    private int authTokenId;
    private String serviceUniqueId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fill(ResultSet resultSet) throws SQLException
    {
        id = resultSet.getInt("id");
        userId = resultSet.getInt("user_id");
        serviceId = resultSet.getInt("service_id");
        authTokenId = resultSet.getInt("auth_token_id");
        serviceUniqueId = resultSet.getString("service_unique_id");
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");
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
    public int getServiceId()
    {
        return serviceId;
    }

    @Override
    public int getAuthTokenId()
    {
        return authTokenId;
    }

    @Override
    public String getServiceUniqueId()
    {
        return serviceUniqueId;
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
}
