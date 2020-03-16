package com.github.tiniyield.jayield.benchmark;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.jayield.benchmark.alternative.sequence.jayield.benchmark.QueryBenchmark;
import com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.benchmark.JoolBenchmark;
import com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.benchmark.StreamExBenchmark;
import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.GuavaBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.ProtonpackBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.ZiplineBenchmark;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ZipTopArtistAndTrackByCountryBenchmark {

    @Setup
    public void setup() {
        SequenceBenchmarkUtils.assertZipTopArtistAndTrackByCountryBenchmarkValidity();
    }

    // Stream Based benchmarks

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void protonpack(Blackhole bh) {
        ProtonpackBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void guava(Blackhole bh) {
        GuavaBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void zipline(Blackhole bh) {
        ZiplineBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Benchmark
    public void streamEx(Blackhole bh) {
        StreamExBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        QueryBenchmark.zipTopArtistAndTrackByCountry().traverse(bh::consume);
    }

    @Benchmark
    public void jool(Blackhole bh) {
        JoolBenchmark.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

}
