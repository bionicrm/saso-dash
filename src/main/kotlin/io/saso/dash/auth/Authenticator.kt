package io.saso.dash.auth

import io.saso.dash.database.entities.LiveToken

public interface Authenticator
{
    fun findLiveToken(token: String): LiveToken?
}
