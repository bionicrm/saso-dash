package io.saso.dash.auth

import io.saso.dash.database.entities.LiveToken
import java.util.*

public interface Authenticator
{
    fun findLiveToken(token: String): Optional<LiveToken>
}
