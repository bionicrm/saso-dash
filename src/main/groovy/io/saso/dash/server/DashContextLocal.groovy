package io.saso.dash.server
import com.google.inject.Singleton
import io.netty.channel.ChannelHandlerContext

import java.util.concurrent.ConcurrentHashMap

@Singleton
class DashContextLocal implements ContextLocal
{
    private final Map<ChannelHandlerContext, Set> map =
            new ConcurrentHashMap<>()

    @Override
    def <T> T get(ChannelHandlerContext ctx)
    {
        T value = map.ctx.find { it instanceof T } as T

        if (value == null) {
            throw new NoSuchElementException(
                    "No element extending $T could be found")
        }

        return value
    }

    @Override
    def <T> void add(ChannelHandlerContext ctx, T value)
    {
        map.ctx += value
    }

    @Override
    def <T> void remove(ChannelHandlerContext ctx)
    {
        map.ctx.removeIf { it instanceof T }
    }
}
