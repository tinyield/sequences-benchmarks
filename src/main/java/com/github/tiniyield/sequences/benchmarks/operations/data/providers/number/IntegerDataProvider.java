package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class IntegerDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public IntegerDataProvider(int size) {
        data = IntegerArrayGenerator.get(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
