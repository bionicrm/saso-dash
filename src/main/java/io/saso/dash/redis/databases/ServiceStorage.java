package io.saso.dash.redis.databases;

import io.saso.dash.services.Service;

import java.util.Map;
import java.util.stream.Stream;

public interface ServiceStorage
{
    /**
     * Pushes the given model into storage, identified by the given service and
     * user ID.
     *
     * @param service the service that "owns" the given model
     * @param userId the ID of the user that "owns" the given model
     * @param model the model to push
     */
    void pushModel(Service service, int userId, Map<String, Object> model);

    /**
     * Pops all the models from storage, which are identified by the given
     * service and user ID; i.e. the models are returned and then removed from
     * storage.
     *
     * @param service the service that "owns" the models
     * @param userId the ID of the user that "owns" the models
     *
     * @return all models for the given service and user ID
     */
    Stream<Map<String, Object>> popModels(Service service, int userId);
}
