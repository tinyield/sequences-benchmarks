package com.github.tiniyield.sequences.benchmarks.data.providers;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.BENCHMARK_GENERATED_DATA_SIZE;

import com.github.tiniyield.sequences.benchmarks.data.generator.ValueGenerator;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

public class ValueDataProvider extends AbstractBaseDataProvider<Value> {

    private final Value[] data;

    public ValueDataProvider() {
        this.data = ValueGenerator.get(BENCHMARK_GENERATED_DATA_SIZE);
    }

    @Override
    protected Value[] data() {
        return data;
    }
}
