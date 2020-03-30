package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class EvenSequenceDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public EvenSequenceDataProvider(int size) {
        data = IntegerArrayGenerator.getAllEven(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
