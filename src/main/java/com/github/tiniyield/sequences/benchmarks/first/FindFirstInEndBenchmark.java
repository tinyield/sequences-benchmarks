package com.github.tiniyield.sequences.benchmarks.first;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenExceptEndDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInEndBenchmark extends FindFirstBenchmark {

    @Setup
    public void init() {
        provider = getEvenExceptEndDataProvider(COLLECTION_SIZE);
    }

}
