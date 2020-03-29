package com.github.tiniyield.sequences.benchmarks.data.generator;

import java.util.stream.Stream;

import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

public class ValueGenerator {

    public static Value[] get(int size) {
        return Stream.of(NumberGenerator.get(size))
                     .map(Value::new)
                     .toArray(Value[]::new);
    }
}
