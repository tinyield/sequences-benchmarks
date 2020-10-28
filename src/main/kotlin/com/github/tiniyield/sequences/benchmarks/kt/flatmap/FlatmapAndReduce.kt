package com.github.tiniyield.sequences.benchmarks.kt.flatmap

fun flatMapAndReduce(input: Sequence<Sequence<Int?>>): Int {
    return input.flatMap { i -> i }
            .reduce { acc: Int?, i: Int? -> acc!!.plus(i!!) }!!
}