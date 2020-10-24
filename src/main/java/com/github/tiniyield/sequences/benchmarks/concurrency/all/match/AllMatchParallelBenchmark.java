package com.github.tiniyield.sequences.benchmarks.concurrency.all.match;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenSequenceDataProvider;
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

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchParallelBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;
    private EvenSequenceDataProvider provider;

    @Setup
    public void setup() {
        super.init();
        provider = getEvenDataProvider(COLLECTION_SIZE);
    }

    private boolean getStream() {
        return stream.isEveryEven(provider.asStream());
    }

    private boolean getStreamParallel() {
        return stream.isEveryEven(provider.asStream().parallel());
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
