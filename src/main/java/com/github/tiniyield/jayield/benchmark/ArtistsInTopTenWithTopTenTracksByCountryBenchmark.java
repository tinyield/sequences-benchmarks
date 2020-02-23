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
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.query.benchmark.JayieldBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.GuavaBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamFlatmapWithIteratorBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamUtilsBenchmark;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Fork(value = 10, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark {

    @Setup
    public void setup() {
        SequenceBenchmarkUtils.assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity();
    }

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamBenchmark.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        JayieldBenchmark.artistsInTopTenWithTopTenTracksByCountry().traverse(bh::consume);
    }

    @Benchmark
    public void streamUtils(Blackhole bh) {
        StreamUtilsBenchmark.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void guava(Blackhole bh) {
        GuavaBenchmark.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void streamFlatMapWithIterator(Blackhole bh) {
        StreamFlatmapWithIteratorBenchmark.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

}
