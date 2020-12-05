package com.github.tiniyield.sequences.benchmarks.kt.odd.lines

class YieldOddLines<T>(private val upstream: Sequence<T>) : Sequence<T> {
    override fun iterator() = iterator {
        val iterator = upstream.iterator();
        var skip = true;
        while (iterator.hasNext()){
            if(skip) {
                iterator.next();
                skip = false;
            } else{
                yield(iterator.next());
                skip = true;
            }
        }
    };
}

fun <T> Sequence<T>.yieldOddLines() = yldOddLines(this)
fun <T> yldOddLines(upstream: Sequence<T>): Sequence<T> = sequence {
    var iterator = upstream.iterator();
    var skip = true;
    for (item in iterator) {
        if (!skip) {
             yield(item)
        }
        skip = !skip
    }
}
