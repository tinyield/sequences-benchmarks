package com.github.tiniyield.sequences.benchmarks.operations.data.providers.object;

import com.github.tiniyield.sequences.benchmarks.operations.data.generator.ValueGenerator;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

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
