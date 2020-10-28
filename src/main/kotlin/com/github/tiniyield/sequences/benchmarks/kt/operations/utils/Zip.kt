package com.github.tiniyield.sequences.benchmarks.kt.operations.utils

class Zip<T, R>(private val upstream: Sequence<T>, private val other: Sequence<R>) : Sequence<Pair<T, R>> {
    override fun iterator(): Iterator<Pair<T, R>> {
        return object : Iterator<Pair<T, R>> {
            val upstreamIt = upstream.iterator()
            val otherIt = other.iterator()

            override fun hasNext(): Boolean {
                return upstreamIt.hasNext() && otherIt.hasNext()
            }

            override fun next(): Pair<T, R> {
                return upstreamIt.next() to otherIt.next()
            }
        }
    }
}

fun <T, R> Sequence<T>.zip(other: Sequence<R>) = Zip(this, other)
