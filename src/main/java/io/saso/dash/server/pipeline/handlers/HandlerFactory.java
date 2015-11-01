package io.saso.dash.server.pipeline.handlers;

import com.google.inject.name.Named;
import io.netty.channel.ChannelHandler;

public interface HandlerFactory
{
    @Named("RequestMethod")
    ChannelHandler createRequestMethod();

    @Named("RequestValidation")
    ChannelHandler createValidation();

    @Named("Upgrading")
    ChannelHandler createUpgrading();
}
