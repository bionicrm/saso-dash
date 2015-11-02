package io.saso.dash.server.handlers.ws;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.saso.dash.server.Client;
import io.saso.dash.server.ClientFactory;
import io.saso.dash.services.ServiceScheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class ServicesHandler extends ChannelHandlerAdapter
{
    private static final Logger logger = LogManager.getLogger();

    private final Provider<ServiceScheduler> serviceSchedulerProvider;
    private final ClientFactory clientFactory;

    @Inject
    public ServicesHandler(Provider<ServiceScheduler> serviceSchedulerProvider,
                           ClientFactory clientFactory)
    {
        this.serviceSchedulerProvider = serviceSchedulerProvider;
        this.clientFactory = clientFactory;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx)
    {
        Client client = clientFactory.create(ctx);
        ServiceScheduler serviceScheduler = serviceSchedulerProvider.get();

        serviceScheduler.schedule(client);
        ctx.channel().closeFuture().addListener(future ->
                serviceScheduler.cancel(client));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
