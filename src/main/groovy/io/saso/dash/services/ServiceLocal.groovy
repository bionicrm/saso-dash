package io.saso.dash.services

interface ServiceLocal
{
    def <T> T get(Service service)

    void set(Service service, value)

    def <T> void remove(Service service)
}
