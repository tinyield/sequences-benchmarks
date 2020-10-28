package com.github.tiniyield.sequences.benchmarks.first;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenExceptStartDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInBeginningBenchmark extends FindFirstBenchmark {

    @Setup
    public void init() {
        provider = getEvenExceptStartDataProvider(COLLECTION_SIZE);
    }

}
