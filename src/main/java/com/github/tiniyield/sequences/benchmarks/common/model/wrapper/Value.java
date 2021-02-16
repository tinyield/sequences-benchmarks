package com.github.tiniyield.sequences.benchmarks.common.model.wrapper;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Value implements Comparable<Value> {
    public final int source;
    public final String text;

    public Value(int value) {
        this.source = value;
        this.text = new StringBuilder(String.valueOf(value)).reverse().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Value value1 = (Value) o;
        return source == value1.source && text.equals(value1.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, text);
    }

    @Override
    public int compareTo(@NotNull Value o) {
        return Integer.compare(source, o.source);
    }
}
