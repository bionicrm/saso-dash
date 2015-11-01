package io.saso.dash.database.scripts;

import com.google.inject.name.Named;

public interface SQLScriptFactory
{
    @Named("find_auth_token")
    EntityReturnableSQLScript createFindAuthToken(int userId,
                                                  String providerName);

    @Named("find_live_token")
    EntityReturnableSQLScript createFindLiveToken(String token);
}
