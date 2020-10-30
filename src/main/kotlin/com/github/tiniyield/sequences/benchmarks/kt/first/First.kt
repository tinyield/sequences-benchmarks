package com.github.tiniyield.sequences.benchmarks.kt.first

import com.github.tiniyield.sequences.benchmarks.first.IsOdd.isOdd

fun findFirst(numbers: Sequence<Int?>): Int? {
    return numbers.find { value: Int? -> isOdd(value) }
}
