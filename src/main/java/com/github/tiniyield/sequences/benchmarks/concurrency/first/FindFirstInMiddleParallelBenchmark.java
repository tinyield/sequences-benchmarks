package com.github.tiniyield.sequences.benchmarks.concurrency.first;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenExceptMiddleDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInMiddleParallelBenchmark extends FindFirstParallelBenchmark {

    @Setup
    public void init() {
        provider = getEvenExceptMiddleDataProvider(COLLECTION_SIZE);
    }

}
