package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.NestedIntegerDataProvider;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FlatMapAndReduceBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    private NestedIntegerDataProvider provider;



    @Setup
    public void setup() {
        super.init();
        provider = new NestedIntegerDataProvider(COLLECTION_SIZE);
    }

    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(stream.flatMapAndReduce(provider.asStream()));
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(streamEx.flatMapAndReduce(provider.asStreamEx()));
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(query.flatMapAndReduce(provider.asQuery()));
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(jool.flatMapAndReduce(provider.asSeq()));
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(vavr.flatMapAndReduce(provider.asVavrStream()));
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(kotlin.flatMapAndReduce(provider.asSequence()));
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(jkotlin.flatMapAndReduce(provider.asSequence()));
    }
}
