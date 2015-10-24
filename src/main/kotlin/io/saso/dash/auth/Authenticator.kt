package io.saso.dash.auth

import io.saso.dash.database.entities.DBLiveToken
import java.util.*

public interface Authenticator
{
    fun authenticate(token: String, onAuthentication: (DBLiveToken) -> Unit,
                     onFailure: () -> Unit)
}
