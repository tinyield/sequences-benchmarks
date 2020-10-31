package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value
import com.github.tiniyield.sequences.benchmarks.zip.IsPrime
import org.javatuples.Pair

fun zipPrimeWithValue(numbers: Sequence<Int>, values: Sequence<Value>): Sequence<Pair<Int, Value>> {
    return numbers.filter { value: Int -> IsPrime.isPrime(value) }
            .zip(values) { value0: Int, value1: Value -> Pair.with(value0, value1) }
}
