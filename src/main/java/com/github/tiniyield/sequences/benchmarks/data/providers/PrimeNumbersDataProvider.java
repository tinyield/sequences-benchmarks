package com.github.tiniyield.sequences.benchmarks.data.providers;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.BENCHMARK_GENERATED_DATA_SIZE;

import com.github.tiniyield.sequences.benchmarks.data.generator.PrimeGenerator;

public class PrimeNumbersDataProvider extends AbstractBaseDataProvider<Integer> {

    private final Integer[] data;

    public PrimeNumbersDataProvider() {
        data = PrimeGenerator.get(BENCHMARK_GENERATED_DATA_SIZE);
    }

    @Override
    protected Integer[] data() {
        return data;
    }
}
