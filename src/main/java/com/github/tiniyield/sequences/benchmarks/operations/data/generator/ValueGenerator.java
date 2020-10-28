package com.github.tiniyield.sequences.benchmarks.operations.data.generator;

import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

import java.util.stream.Stream;

public class ValueGenerator {

    public static Value[] get(int size) {
        return Stream.of(IntegerArrayGenerator.box(IntegerArrayGenerator.get(size)))
                     .map(Value::new)
                     .toArray(Value[]::new);
    }
}
