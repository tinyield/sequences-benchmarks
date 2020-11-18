package com.github.tiniyield.sequences.benchmarks.kt.collapse

class Collapse<T>(private val upstream: Sequence<T>) : Sequence<T> {
    override fun iterator() = object : Iterator<T> {
        val iterator = upstream.iterator()
        var prev: T? = null
        var curr: T? = null

        fun moveNext() {
            if (curr != null) {
                return
            } else {
                while (iterator.hasNext() && curr == null) {
                    val aux = iterator.next()
                    if (aux != null && aux != prev)
                        curr = aux
                }
            }
        }

        override fun hasNext(): Boolean {
            if (curr == null) {
                moveNext()
            }
            return curr != null
        }

        override fun next(): T {
            if (curr == null) {
                moveNext()
                if (curr == null) {
                    throw NoSuchElementException()
                }
            }
            prev = curr
            curr = null
            return prev!!
        }

    }
}
fun <T> Sequence<T>.collapse() = Collapse(this)
