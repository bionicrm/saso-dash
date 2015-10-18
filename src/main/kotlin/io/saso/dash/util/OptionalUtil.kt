package io.saso.dash.util

import java.util.*

fun <T> Optional<T>.ifPresent(toDo: (T) -> Unit, orElse: () -> Unit) {
    if (isPresent) {
        toDo(get())
    }
    else {
        orElse()
    }
}
