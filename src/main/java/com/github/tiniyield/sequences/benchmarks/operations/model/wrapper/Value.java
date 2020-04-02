package com.github.tiniyield.sequences.benchmarks.operations.model.wrapper;

import java.util.Objects;

public class Value {
    public final int value;
    public final String text;

    public Value(int value) {
        this.value = value;
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
        return value == value1.value && text.equals(value1.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, text);
    }
}
