package com.github.tiniyield.sequences.benchmarks.kt.odd.lines

class OddLines<T>(private val upstream: Sequence<T>) : Sequence<T> {
    override fun iterator() = object : Iterator<T> {
        val iterator = upstream.iterator()
        var curr: T? = null

        fun moveNext() {
            if (curr != null) {
                return
            } else {
                var skip = true
                while (iterator.hasNext() && curr == null) {
                    if (!skip) {
                        curr = iterator.next()
                    } else {
                        iterator.next()
                        skip = false
                    }
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
            val aux = curr!!
            curr = null
            return aux
        }
    }
}

fun <T> Sequence<T>.oddLines() = OddLines(this)
