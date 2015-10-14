package io.saso.dash.services;

import io.saso.dash.database.entities.AuthToken;
import io.saso.dash.database.entities.Provider;
import io.saso.dash.database.entities.ProviderUser;
import io.saso.dash.database.entities.User;

public interface DBEntityProvider
{
    User user() throws Exception;

    Provider provider(Service service) throws Exception;

    ProviderUser providerUser(Service service) throws Exception;

    AuthToken authToken(Service service) throws Exception;
}
