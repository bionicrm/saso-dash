package io.saso.dash.util

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

fun <T, R : Throwable> tryResources(toTry: ResourceHolder.() -> T,
                                    catch: (R) -> T): T
{
    var holder = ResourceHolder()

    try {
        return holder.toTry()
    }
    catch (e: R) {
        return catch(e)
    }
    finally {
        holder.close()
    }
}
