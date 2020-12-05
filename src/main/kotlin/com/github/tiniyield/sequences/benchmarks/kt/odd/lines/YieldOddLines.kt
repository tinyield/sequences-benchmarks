package com.github.tiniyield.sequences.benchmarks.kt.odd.lines

fun <T> Sequence<T>.yieldOddLines() = sequence {
    var iterator = this@yieldOddLines.iterator()
    var skip = true
    for (item in iterator) {
        if (!skip) {
            yield(item)
        }
        skip = !skip
    }
}
