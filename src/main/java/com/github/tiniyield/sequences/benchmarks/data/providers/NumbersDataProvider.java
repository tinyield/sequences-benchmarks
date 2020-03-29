package com.github.tiniyield.sequences.benchmarks.data.providers;

import com.github.tiniyield.sequences.benchmarks.data.generator.NumberGenerator;

public class NumbersDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public NumbersDataProvider(int size) {
        data = NumberGenerator.get(size);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
