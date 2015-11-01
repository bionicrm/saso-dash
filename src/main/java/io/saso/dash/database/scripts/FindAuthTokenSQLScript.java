package io.saso.dash.database.scripts;

public interface FindAuthTokenSQLScript extends EntityReturnableSQLScript
{
    void setUserId(int userId);

    void setProviderName(String providerName);
}
