package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class FindFirstBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    protected AbstractBaseDataProvider<Integer> provider;

    @Setup
    public void setup() {
        super.init();
        init();
        SequenceBenchmarkUtils.assertFindResult(getJool(), getStream(), getStreamEx(), getQuery(), getVavr());
    }

    protected abstract void init();

    private Integer getJool() {
        return jool.findFirst(provider.asSeq()).orElseThrow();
    }

    private Integer getStream() {
        return stream.findFirst(provider.asStream()).orElseThrow();
    }

    private Integer getStreamEx() {
        return streamEx.findFirst(provider.asStreamEx()).orElseThrow();
    }

    private Integer getQuery() {
        return query.findFirst(provider.asQuery()).orElseThrow();
    }

    private Integer getVavr() {
        return vavr.findFirst(provider.asVavrStream()).getOrElseThrow(RuntimeException::new);
    }

    private Integer getKotlin() {
        return kotlin.findFirst(provider.asSequence()).orElseThrow(RuntimeException::new);
    }

    private Integer getJKotlin() {
        return jkotlin.findFirst(provider.asSequence()).orElseThrow(RuntimeException::new);
    }

    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }

    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(getStreamEx());
    }

    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(getQuery());
    }

    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(getJool());
    }


    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(getVavr());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(getKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(getJKotlin());
    }

}
