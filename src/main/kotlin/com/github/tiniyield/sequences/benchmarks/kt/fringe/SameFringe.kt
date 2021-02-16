package com.github.tiniyield.sequences.benchmarks.kt.fringe

import com.github.tiniyield.sequences.benchmarks.fringe.BinTree
import java.util.function.BiPredicate

fun <T : Comparable<T>?, U : Comparable<U>?> sameFringe(q1: BinTree<T>, q2: BinTree<U>, predicate: BiPredicate<T, U>): Boolean {
    return q1.zip(q2) { t: T, u: U -> predicate.test(t, u) }.all(java.lang.Boolean.TRUE::equals)
}
