package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class EvenExceptEndSequenceDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public EvenExceptEndSequenceDataProvider(int size) {
        data = IntegerArrayGenerator.getAllEvenExceptEnd(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
