package com.github.tiniyield.sequences.benchmarks.first;

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
public abstract class FindFirstBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    protected int COLLECTION_SIZE;
    protected AbstractBaseDataProvider<Integer> provider;
    private JoolOperations jool;
    private QueryOperations query;

    protected abstract void init();

    @Setup
    public void setup() {
        init();
        jool = new JoolOperations();
        query = new QueryOperations();
        SequenceBenchmarkUtils.assertFindResult(getJool(), getStream(), getStreamEx(), getQuery(), getVavr());
    }

    private Integer getVavr() {
        return VavrOperations.findFirst(provider).getOrElseThrow(RuntimeException::new);
    }

    private Integer getQuery() {
        return query.findFirst(provider.asQuery()).orElseThrow();
    }

    private Integer getStreamEx() {
        return StreamExOperations.findFirst(provider).orElseThrow();
    }

    private Integer getStream() {
        return StreamOperations.findFirst(provider).orElseThrow();
    }

    private Integer getJool() {
        return jool.findFirst(provider.asSeq()).orElseThrow();
    }

    @Override
    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }

    @Override
    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(getStreamEx());
    }

    @Override
    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(getQuery());
    }

    @Override
    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(getJool());
    }


    @Override
    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(getVavr());
    }

}
