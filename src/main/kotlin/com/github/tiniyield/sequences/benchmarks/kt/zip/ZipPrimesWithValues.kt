package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value
import com.github.tiniyield.sequences.benchmarks.zip.IsPrime
import org.javatuples.Pair


/**
 * Filters the kotlin prime numbers {@link Sequence} in Java from the {@param numbers} and then zips the resulting
 * {@link Sequence} in Java with the {@param values}
 * @param numbers the numbers to filter
 * @param values the values to pair with
 * @return a Kotlin {@link Sequence} in Java of Pairs between prime numbers and values
 */
fun zipPrimeWithValue(numbers: Sequence<Int>, values: Sequence<Value>): Sequence<Pair<Int, Value>> {
    return numbers.filter { value: Int -> IsPrime.isPrime(value) }
            .zip(values) { value0: Int, value1: Value -> Pair.with(value0, value1) }
}
