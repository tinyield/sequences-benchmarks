package com.github.tiniyield.sequences.benchmarks.first;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

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
