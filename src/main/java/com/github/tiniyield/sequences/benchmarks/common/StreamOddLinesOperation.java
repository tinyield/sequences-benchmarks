package com.github.tiniyield.sequences.benchmarks.common;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class StreamOddLinesOperation {

    public static class StreamOddLines<T> extends Spliterators.AbstractSpliterator<T> {

        final Consumer<T> doNothing = item -> {
        };
        final Spliterator<T> source;
        boolean isOdd;
        public StreamOddLines(Spliterator<T> source) {
            super(odd(source.estimateSize()), source.characteristics());
            this.source = source;
        }

        private static long odd(long l) {
            return l == Long.MAX_VALUE ? l : (l + 1) / 2;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (!source.tryAdvance(doNothing)) return false;
            return source.tryAdvance(action);
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            isOdd = false;
            source.forEachRemaining(item -> {
                if (isOdd) action.accept(item);
                isOdd = !isOdd;
            });
        }
    }

}
