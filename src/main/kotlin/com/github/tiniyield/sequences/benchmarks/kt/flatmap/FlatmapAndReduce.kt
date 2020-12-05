package com.github.tiniyield.sequences.benchmarks.kt.flatmap

/**
 * Maps the nested Kotlin {@link Sequence} in Kotlin into an {@link Integer} Kotlin {@link Sequence} in Kotlin
 * and reduces it by summing all values.
 * @param input the nested sequence
 * @return the sum of all values
 */
fun flatMapAndReduce(input: Sequence<Sequence<Int?>>): Int {
    return input.flatMap { i -> i }
        .reduce { acc: Int?, i: Int? -> acc!!.plus(i!!) }!!
}
