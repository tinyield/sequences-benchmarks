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

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class FindFirstBenchmark extends AbstractSequenceOperationsBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    protected int COLLECTION_SIZE;
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

    @Override
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(getKotlin());
    }

}
