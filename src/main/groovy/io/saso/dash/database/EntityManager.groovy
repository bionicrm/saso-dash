package io.saso.dash.database

interface EntityManager
{
    def <T extends DBEntity> Optional<T> execute(
            Class<T> entityClass, String sql, Object... params)
}
