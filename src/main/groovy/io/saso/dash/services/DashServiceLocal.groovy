package io.saso.dash.services

import com.google.inject.Singleton

import java.util.concurrent.ConcurrentHashMap

@Singleton
class DashServiceLocal implements ServiceLocal
{
    private final Map<Service, Set> map = new ConcurrentHashMap()

    @Override
    def <T> T get(Service service)
    {
        return map[service].find { it instanceof T } as T
    }

    @Override
    void set(Service service, value)
    {
        if (map[service] == null) {
            map[service] = new HashSet()
        }

        map[service] << value
    }

    @Override
    def <T> void remove(Service service)
    {
        map[service].removeIf { it instanceof T }

        if (map[service].empty) {
            map.remove service
        }
    }
}
