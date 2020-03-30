package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class EvenExceptMiddleSequenceDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public EvenExceptMiddleSequenceDataProvider(int size) {
        data = IntegerArrayGenerator.getAllEvenExceptMiddle(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
