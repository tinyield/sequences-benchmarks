package com.github.tiniyield.sequences.benchmarks.kt.collapse

class YieldCollapse<T>(private val upstream: Sequence<T>) : Sequence<T> {
    override fun iterator() = iterator {
        val iterator = upstream.iterator()
        var prev: T? = null
        while (iterator.hasNext()) {
            val aux = iterator.next()
            if (aux != null && aux != prev) {
                prev = aux
                yield(aux)
            }
        }
    }
}

fun <T> Sequence<T>.yieldCollapse() = YieldCollapse(this)
