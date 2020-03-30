package com.github.tiniyield.sequences.benchmarks.find;

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
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class FindBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    protected int COLLECTION_SIZE;
    protected AbstractBaseDataProvider<Integer> provider;
    protected abstract void init();

    @Setup
    public void setup() {
        init();
        SequenceBenchmarkUtils.assertFindResult(provider);
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(StreamOperations.find(provider));
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(StreamExOperations.find(provider));
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(QueryOperations.find(provider));
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(JoolOperations.find(provider));
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(VavrOperations.find(provider));
    }

}
