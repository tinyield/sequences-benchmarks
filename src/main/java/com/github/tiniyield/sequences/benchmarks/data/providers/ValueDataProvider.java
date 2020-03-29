package com.github.tiniyield.sequences.benchmarks.data.providers;

import com.github.tiniyield.sequences.benchmarks.data.generator.ValueGenerator;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

public class ValueDataProvider extends AbstractBaseDataProvider<Value> {

    private final Value[] data;

    public ValueDataProvider(int size) {
        this.data = ValueGenerator.get(size);
    }

    @Override
    protected Value[] data() {
        return data;
    }
}
