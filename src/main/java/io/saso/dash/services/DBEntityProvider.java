package io.saso.dash.services;

import io.saso.dash.database.entities.*;

public interface DBEntityProvider
{
    User user() throws Exception;

    Provider provider(Service service) throws Exception;

    ProviderUser providerUser(Service service) throws Exception;

    AuthToken authToken(Service service) throws Exception;
}
