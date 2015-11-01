package io.saso.dash.database.scripts.impl;

import io.saso.dash.database.DBEntity;
import io.saso.dash.database.entities.DBAuthToken;
import io.saso.dash.database.scripts.FindAuthTokenSQLScript;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DashFindAuthTokenSQLScript implements FindAuthTokenSQLScript
{
    private int userId;
    private String providerName;

    @Override
    public Class<? extends DBEntity> getEntityClass()
    {
        return DBAuthToken.class;
    }

    @Override
    public void setParametersOn(PreparedStatement statement) throws SQLException
    {
        statement.setInt(1, userId);
        statement.setString(2, providerName);
    }

    @Override
    public String getPath()
    {
        return "find_auth_token.sql";
    }

    @Override
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    @Override
    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }
}
