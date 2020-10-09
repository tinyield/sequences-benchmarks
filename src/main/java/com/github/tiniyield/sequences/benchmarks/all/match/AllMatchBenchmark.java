package com.github.tiniyield.sequences.benchmarks.all.match;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertEveryEvenValidity;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;

import java.util.concurrent.TimeUnit;

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

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchBenchmark extends AbstractSequenceOperationsBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;
    private EvenSequenceDataProvider provider;

    @Setup
    public void setup() {
        super.init();
        provider = getEvenDataProvider(COLLECTION_SIZE);
        assertEveryEvenValidity(getStream(), getStreamEx(), getQuery(), getJool(), getVavr());
    }

    private boolean getStream() {
        return stream.isEveryEven(provider.asStream());
    }

    private boolean getStreamEx() {
        return streamEx.isEveryEven(provider.asStreamEx());
    }

    private boolean getQuery() {
        return query.isEveryEven(provider.asQuery());
    }

    private boolean getJool() {
        return jool.isEveryEven(provider.asSeq());
    }

    private boolean getVavr() {
        return vavr.isEveryEven(provider.asVavrStream());
    }

    private boolean getKotlin() {
        return kotlin.isEveryEven(provider.asSequence());
    }

    private boolean getJKotlin() {
        return jkotlin.isEveryEven(provider.asSequence());
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(getStreamEx());
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(getQuery());
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(getJool());
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(getVavr());
    }

    @Override
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(getKotlin());
    }

    @Override
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(getJKotlin());
    }

}
