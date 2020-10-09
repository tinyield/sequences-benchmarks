package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.IntegerArrayGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

public class IntegerDataProvider extends AbstractBaseDataProvider<Integer> {

    private final int[] data;
    private final Integer[] boxed;

    public IntegerDataProvider(int size) {
        data = IntegerArrayGenerator.get(size);
        boxed = IntegerArrayGenerator.box(data);
    }

    @Override
    protected Integer[] data() {
        return boxed;
    }

    public int[] unboxed() {
        return data;
    }
}
