package io.saso.dash.util

fun key(vararg parts: Any): String
{
    val str = StringBuilder()

    parts.forEachIndexed { i, p ->
        str append p

        if (i != parts.lastIndex) {
            str append ':'
        }
    }

    return str.toString()
}
