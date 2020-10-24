package com.github.tiniyield.sequences.benchmarks.concurrency.distinct;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
import com.pivovarit.collectors.ParallelCollectors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class DistinctParallelCollectorsBenchmark {
    @Param({"10000"})
    private int COLLECTION_SIZE;

    @Param({"2"})
    private int PARALLELISM;
    private IntegerDataProvider provider;
    private ExecutorService executor;

    @Setup
    public void setup() {
        provider = new IntegerDataProvider(COLLECTION_SIZE);
        executor = Executors.newFixedThreadPool(PARALLELISM);
    }

    @TearDown
    public void tearDown() {
        executor.shutdown();
    }


    @Benchmark
    public void parallelCollectors(Blackhole bh) {
        Arrays.stream(provider.unboxed())
                .distinct()
                .boxed()
                .collect(ParallelCollectors.parallel(String::valueOf, toSet(), executor, PARALLELISM))
                .thenAccept(set -> bh.consume(set.size()));
    }

    @Benchmark
    public void parallel(Blackhole bh) {
        bh.consume(
                Arrays.stream(provider.unboxed())
                        .parallel()
                        .distinct()
                        .boxed()
                        .map(String::valueOf)
                        .collect(Collectors.toSet())
                        .size()
        );
    }

    @Benchmark
    public void sequential(Blackhole bh) {
        bh.consume(
                Arrays.stream(provider.unboxed())
                .distinct()
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toSet())
                .size()
        );
    }
}
