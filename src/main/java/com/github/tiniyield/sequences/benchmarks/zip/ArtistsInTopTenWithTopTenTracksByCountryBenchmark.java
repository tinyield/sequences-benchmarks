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

import com.github.tiniyield.sequences.benchmarks.IZipBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.GuavaOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ProtonpackOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark implements IZipBenchmark {

    @Setup
    public void setup() {
        SequenceBenchmarkUtils.assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity();
    }

    // Stream Based benchmarks

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void protonpack(Blackhole bh) {
        ProtonpackOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void guava(Blackhole bh) {
        GuavaOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void zipline(Blackhole bh) {
        ZiplineOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        StreamExOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        QueryOperations.artistsInTopTenWithTopTenTracksByCountry().traverse(bh::consume);
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        JoolOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        VavrOperations.artistsInTopTenWithTopTenTracksByCountry().forEach(bh::consume);
    }

}
