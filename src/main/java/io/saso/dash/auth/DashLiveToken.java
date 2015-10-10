package io.saso.dash.auth;

import io.saso.dash.util.LoggingUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DashLiveToken implements LiveToken
{
    private int       id;
    private int       userId;
    private String    token;
    private String    ip;
    private Timestamp expiresAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public void fillFromResultSet(ResultSet resultSet) throws SQLException
    {
        id        = resultSet.getInt("id");
        userId    = resultSet.getInt("user_id");
        token     = resultSet.getString("token");
        ip        = resultSet.getString("ip");
        expiresAt = resultSet.getTimestamp("expires_at");
        createdAt = resultSet.getTimestamp("created_at");
        updatedAt = resultSet.getTimestamp("updated_at");

        LoggingUtil.logResultSet(resultSet, getClass());
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
    public String getToken()
    {
        return token;
    }

    @Override
    public String getIp()
    {
        return ip;
    }

    @Override
    public Timestamp getExpiresAt()
    {
        return expiresAt;
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
