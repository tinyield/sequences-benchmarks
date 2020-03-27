package com.github.tiniyield.sequences.benchmarks.zip;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.GuavaBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.JoolBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.ProtonpackBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.QueryBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.StreamBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.StreamExBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.ZiplineBenchmark;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VanillaZipBenchmark implements IZipBenchmark {

    @Setup
    public void setup() {

    }

    // Stream Based benchmarks

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void protonpack(Blackhole bh) {
        ProtonpackBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void guava(Blackhole bh) {
        GuavaBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void zipline(Blackhole bh) {
        ZiplineBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        StreamExBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        QueryBenchmark.zipPrimeWithValue().traverse(bh::consume);
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        JoolBenchmark.zipPrimeWithValue().forEach(bh::consume);
    }

}
