package com.github.tiniyield.sequences.benchmarks.operations;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class TestDataProvider<T> extends AbstractBaseDataProvider<T> {

    private final T[] data;

    public TestDataProvider(T... data) {
        this.data = data;
    }

    @Override
    protected T[] data() {
        return data;
    }
}
