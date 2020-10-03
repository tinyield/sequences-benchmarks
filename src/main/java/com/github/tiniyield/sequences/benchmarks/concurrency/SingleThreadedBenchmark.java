package com.github.tiniyield.sequences.benchmarks.concurrency;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SingleThreadedBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;
    private IntegerDataProvider provider;

    @Setup
    public void setup() {
        provider = new IntegerDataProvider(COLLECTION_SIZE);
    }

    @Benchmark
    public void one(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void two(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void three(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void four(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void five(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void six(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void seven(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }
    @Benchmark
    public void eight(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void nine(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void ten(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void eleven(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void twelve(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void thirteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void fourteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void fifteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void sixteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void seventeen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void eighteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }

    @Benchmark
    public void nineteen(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .forEach(bh::consume);
    }

    @Benchmark
    public void twenty(Blackhole bh) {
        provider.asStream()
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .map(SequenceBenchmarkUtils::increment)
                .filter(SequenceBenchmarkUtils::isPositive)
                .forEach(bh::consume);
    }
}





























