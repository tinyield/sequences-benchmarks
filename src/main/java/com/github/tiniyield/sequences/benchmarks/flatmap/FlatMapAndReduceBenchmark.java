package com.github.tiniyield.sequences.benchmarks.flatmap;

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
public class FlatMapAndReduceBenchmark extends AbstractSequenceOperationsBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;
    private NestedIntegerDataProvider provider;



    @Setup
    public void setup() {
        super.init();
        provider = new NestedIntegerDataProvider(COLLECTION_SIZE);
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(stream.flatMapAndReduce(provider.asStream()));
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(streamEx.flatMapAndReduce(provider.asStreamEx()));
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(query.flatMapAndReduce(provider.asQuery()));
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(jool.flatMapAndReduce(provider.asSeq()));
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(vavr.flatMapAndReduce(provider.asVavrStream()));
    }

    @Override
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(kotlin.flatMapAndReduce(provider.asSequence()));
    }

    @Override
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(jkotlin.flatMapAndReduce(provider.asSequence()));
    }
}
