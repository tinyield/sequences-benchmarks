package com.github.tiniyield.sequences.benchmarks.kt.fringe

import com.github.tiniyield.sequences.benchmarks.fringe.BinTree
import java.util.function.BiPredicate

fun <T : Comparable<T>?, U : Comparable<U>?> sameFringe(q1: BinTree<T>, q2: BinTree<U>, predicate: BiPredicate<T, U>): Boolean {
    return getLeaves(q1).zip(getLeaves(q2)) { t: T, u: U -> predicate.test(t, u) }.all(java.lang.Boolean.TRUE::equals)
}

fun <T : Comparable<T>?> getLeaves(src: BinTree<T>) : Sequence<T> {
    return sequence {
        if(src.isLeaf()) {
            yield(src.`val`)
        }
        else {
            if(src.left != null)
                getLeaves(src.left).forEach { leaf -> yield(leaf) }
            if(src.right != null)
                getLeaves(src.right).forEach { leaf -> yield(leaf) }
        }
    }
}
