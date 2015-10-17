package io.saso.dash.util

import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream

public class ResourceHolder : AutoCloseable
{
    val resources: MutableList<AutoCloseable> = arrayListOf()

    fun <T : AutoCloseable> T.autoClose(): T
    {
        resources.add(this)

        return this
    }

    override fun close()
    {
        resources.reversed().forEach { it.close() }
    }
}

fun <T> tryResources(toTry: ResourceHolder.() -> T): T
{
    val holder = ResourceHolder()

    try {
        return holder.toTry()
    }
    finally {
        holder.close()
    }
}

fun <T, X : Throwable> tryResources(toTry: ResourceHolder.() -> T,
                                    catch: (X) -> T): T
{
    var holder = ResourceHolder()

    try {
        return holder.toTry()
    }
    catch (e: X) {
        return catch(e)
    }
    finally {
        holder.close()
    }
}

object Resources
{
    fun get(path: String): String
    {
        if (path startsWith '/') {
            return IOUtils.toString(javaClass.getResource(path))
        }
        else {
            return IOUtils.toString(FileInputStream(path))
        }
    }
}
