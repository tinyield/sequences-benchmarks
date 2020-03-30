package com.github.tiniyield.sequences.benchmarks.every;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertEveryEvenValidity;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initEvenDataProvider;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class EveryEvenBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;

    @Setup
    public void setup() {
        initEvenDataProvider(COLLECTION_SIZE);
        assertEveryEvenValidity();
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(StreamOperations.everyEven());
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(StreamExOperations.everyEven());
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(QueryOperations.everyEven());
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(JoolOperations.everyEven());
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(VavrOperations.everyEven());
    }

}
