package com.github.tiniyield.sequences.benchmarks.kt.all.match

import com.github.tiniyield.sequences.benchmarks.all.match.AllMatchBenchmark

fun isEveryEven(numbers: Sequence<Int>): Boolean {
    return numbers.all { value: Int -> AllMatchBenchmark.isEven(value) }
}
