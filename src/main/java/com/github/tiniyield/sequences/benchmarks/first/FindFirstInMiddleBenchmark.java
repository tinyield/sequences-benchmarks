package com.github.tiniyield.sequences.benchmarks.first;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenExceptMiddleDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInMiddleBenchmark extends FindFirstBenchmark {

    @Setup
    public void init() {
        provider = getEvenExceptMiddleDataProvider(COLLECTION_SIZE);
    }

}
