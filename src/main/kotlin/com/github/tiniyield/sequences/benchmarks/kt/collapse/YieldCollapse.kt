package com.github.tiniyield.sequences.benchmarks.kt.collapse

fun <T> Sequence<T>.yieldCollapse() = sequence {
    val iterator = this@yieldCollapse.iterator()
    var prev: T? = null
    while (iterator.hasNext()) {
        val aux = iterator.next()
        if (aux != null && aux != prev) {
            prev = aux
            yield(aux)
        }
    }
}
