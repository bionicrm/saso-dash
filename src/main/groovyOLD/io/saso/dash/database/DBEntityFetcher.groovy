package io.saso.dash.database

interface DBEntityFetcher
{
    def <T extends DBEntity> Optional<T> fetch(
            Class<T> entityClass, String sql, ...params)
}
