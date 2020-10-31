package com.github.tiniyield.sequences.benchmarks.kt.every

import java.util.function.BiPredicate

/**
 * Zips two sequences of Kotlin {@link Sequence}s in Kotlin together mapping the combination of each String to a boolean with
 * the BiPredicate.
 * @return true if all Strings in the zipped sequence are true, false otherwise.
 */
fun <T, U> every(q1: Sequence<T>, q2: Sequence<U>, predicate: BiPredicate<T, U>): Boolean {
    return q1.zip(q2) { t: T, u: U -> predicate.test(t, u) }.all(java.lang.Boolean.TRUE::equals)
}
