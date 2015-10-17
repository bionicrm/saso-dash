package io.saso.dash.database;

import java.util.Optional;

public interface EntityManagerOLD
{
    <T extends DBEntity, S extends T> Optional<T> execute(Class<S> entityClass,
                                                          String sql,
                                                          Object... params)
            throws Exception;
}
