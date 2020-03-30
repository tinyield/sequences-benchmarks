package com.github.tiniyield.sequences.benchmarks.operations.model.wrapper;

public class Value {
    public final String text;
    public Value(int val) {
        text = new StringBuilder(String.valueOf(val)).reverse().toString();
    }
}
