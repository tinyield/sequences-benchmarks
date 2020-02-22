package com.github.tiniyield.jayield.benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.query.benchmark.zip.JayieldBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.zip.GuavaBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.zip.StreamBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.zip.StreamUtilsBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.zip.StreamFlatmapWithIteratorBenchmark;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Threads(Threads.MAX)
public class ZipTopArtistAndTrackByCountryBenchmark {

    @Setup
    public void setup() {
        SequenceBenchmarkUtils.assertZipBenchmarkValidity();
    }

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamBenchmark.query().forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        JayieldBenchmark.query().traverse(bh::consume);
    }

    @Benchmark
    public void streamUtils(Blackhole bh) {
        StreamUtilsBenchmark.query().forEach(bh::consume);
    }

    @Benchmark
    public void guava(Blackhole bh) {
        GuavaBenchmark.query().forEach(bh::consume);
    }

    @Benchmark
    public void streamFlatMapWithIterator(Blackhole bh) {
        StreamFlatmapWithIteratorBenchmark.query().forEach(bh::consume);
    }

}
