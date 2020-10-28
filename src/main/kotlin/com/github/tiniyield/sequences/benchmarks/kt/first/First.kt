package com.github.tiniyield.sequences.benchmarks.kt.first

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils

fun findFirst(numbers: Sequence<Int?>): Int? {
    return numbers.find { value: Int? -> SequenceBenchmarkUtils.isOdd(value) }
}
