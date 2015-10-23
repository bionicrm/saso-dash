package io.saso.dash.util

class ResourceHolder : AutoCloseable
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
