package com.github.tiniyield.sequences.benchmarks.concurrency.flatmap;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.NestedIntegerDataProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FlatMapAndReduceParallelBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;
    private NestedIntegerDataProvider provider;



    @Setup
    public void setup() {
        super.init();
        provider = new NestedIntegerDataProvider(COLLECTION_SIZE);
    }

    @Benchmark
    public void parallel(Blackhole bh) {
        bh.consume(stream.flatMapAndReduce(provider.asParallelStream()));
    }

    @Benchmark
    public void sequential(Blackhole bh) {
        bh.consume(stream.flatMapAndReduce(provider.asStream()));
    }

}
