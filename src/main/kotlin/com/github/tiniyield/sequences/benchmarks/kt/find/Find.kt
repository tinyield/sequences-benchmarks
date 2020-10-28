package com.github.tiniyield.sequences.benchmarks.kt.find

import java.util.*
import java.util.function.BiPredicate

fun <T> find(q1: Sequence<T>, q2: Sequence<T>, predicate: BiPredicate<T, T>): T? {
    return q1.zip(q2) { t1, t2 -> if (predicate.test(t1, t2)) t1 else null }
            .filter { obj -> Objects.nonNull(obj) }
            .firstOrNull()
}
