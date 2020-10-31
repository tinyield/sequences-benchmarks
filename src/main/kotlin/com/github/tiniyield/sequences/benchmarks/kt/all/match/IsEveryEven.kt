package com.github.tiniyield.sequences.benchmarks.kt.all.match

import com.github.tiniyield.sequences.benchmarks.all.match.AllMatchBenchmark

/**
 * Checks if every Int in a sequence is Even, using Kotlin {@link Sequence}s in Kotlin
 * @return whether every Int is even or not
 */
fun isEveryEven(numbers: Sequence<Int>): Boolean {
    return numbers.all { value: Int -> AllMatchBenchmark.isEven(value) }
}
