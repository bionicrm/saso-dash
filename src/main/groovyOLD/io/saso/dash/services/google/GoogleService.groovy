package io.saso.dash.services.google

import io.saso.dash.services.services.Service
import io.saso.dash.services.ServiceName

class GoogleService implements Service
{
    final ServiceName name = ServiceName.GOOGLE

    final int pollTime = 30
}
