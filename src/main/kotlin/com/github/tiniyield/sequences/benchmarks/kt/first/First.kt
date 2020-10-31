package com.github.tiniyield.sequences.benchmarks.kt.first

import com.github.tiniyield.sequences.benchmarks.first.IsOdd.isOdd

/**
 * Searches a Kotlin {@link Sequence} in Kotlin for an odd number
 * @param numbers the sequence to search in
 * @return the first odd Integer in the sequence or null if none exists
 */
fun findFirst(numbers: Sequence<Int?>): Int? {
    return numbers.find { value: Int? -> isOdd(value) }
}
