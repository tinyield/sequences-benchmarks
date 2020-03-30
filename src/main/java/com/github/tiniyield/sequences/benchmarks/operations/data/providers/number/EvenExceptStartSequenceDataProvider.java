package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class EvenExceptStartSequenceDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public EvenExceptStartSequenceDataProvider(int size) {
        data = IntegerArrayGenerator.getAllEvenExceptStart(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
