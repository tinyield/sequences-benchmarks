package com.github.tiniyield.sequences.benchmarks.concurrency.first;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class FindFirstParallelBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    protected int COLLECTION_SIZE;
    protected AbstractBaseDataProvider<Integer> provider;

    @Setup
    public void setup() {
        super.init();
        init();
    }

    protected abstract void init();

    private Integer getStream() {
        return stream.findFirst(provider.asStream()).orElseThrow();
    }

    private Integer getStreamParallel() {
        return stream.findFirst(provider.asStream().parallel()).orElseThrow();
    }


    @Benchmark
    public void parallel(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStreamParallel());
    }

    @Benchmark
    public void sequential(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }


}
