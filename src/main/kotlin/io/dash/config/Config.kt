package io.dash.config

public interface Config
{
    fun <T> get(key: String, default: T): T
}
