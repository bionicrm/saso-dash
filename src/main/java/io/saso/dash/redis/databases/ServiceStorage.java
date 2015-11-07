package io.saso.dash.redis.databases;

import io.saso.dash.server.Client;
import io.saso.dash.services.Service;

import java.util.Map;
import java.util.stream.Stream;

public interface ServiceStorage
{
    /**
     * Initializes the service storage Redis DB.
     */
    void initialize();

    /**
     * Pushes the given model into storage, identified by the given service and
     * user ID.
     *
     * @param service the service that "owns" the given model
     * @param client the client that "owns" the given model
     * @param model the model to push
     */
    void pushModel(Service service, Client client, Map<String, Object> model);

    /**
     * Peeks at all the models from storage, which are identified by the given
     * service and user ID; i.e. the models are returned from storage.
     *
     * @param service the service that "owns" the models
     * @param client the client that "owns" the models
     *
     * @return all models for the given service and user ID
     */
    Stream<Map<String, Object>> peekModels(Service service, Client client);
}
