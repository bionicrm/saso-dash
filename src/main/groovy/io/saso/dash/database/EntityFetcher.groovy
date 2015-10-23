package io.saso.dash.database

interface EntityFetcher
{
    def <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String sql, Object... params)
}
