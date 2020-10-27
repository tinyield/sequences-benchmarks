package com.github.tiniyield.sequences.benchmarks.kt.all.match

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils

fun isEveryEven(numbers: Sequence<Int>): Boolean {
    return numbers.all { value: Int -> SequenceBenchmarkUtils.isEven(value) }
}
