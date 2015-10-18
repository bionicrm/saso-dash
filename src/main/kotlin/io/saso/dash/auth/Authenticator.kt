package io.saso.dash.auth

import io.saso.dash.database.entities.LiveToken
import java.util.*

public interface Authenticator
{
    fun authenticate(token: String, onAuthentication: (LiveToken) -> Unit,
                     onFailure: () -> Unit)
}
