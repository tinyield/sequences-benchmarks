package com.github.tiniyield.sequences.benchmarks.collapse;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class StreamCollapseOperation {
    public static class StreamCollapse<T> extends Spliterators.AbstractSpliterator<T> implements Consumer<T> {

        private final Spliterator<T> source;
        private T curr = null;

        public StreamCollapse(Spliterator<T> source) {
            super(Long.MAX_VALUE, source.characteristics() & (~(SIZED | SUBSIZED)));
            this.source = source;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            T prev = curr;
            boolean hasNext;
            while ((hasNext = source.tryAdvance(this)) && curr.equals(prev)) { }
            if(hasNext) action.accept(curr);
            return hasNext;
        }

        @Override
        public void accept(T item) {
            curr = item;
        }
    }
}
