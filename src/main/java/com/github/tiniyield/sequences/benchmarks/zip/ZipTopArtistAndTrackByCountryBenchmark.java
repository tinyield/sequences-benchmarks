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
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ZipTopArtistAndTrackByCountryBenchmark implements IZipBenchmark {

    private GuavaOperations guava;

    @Setup
    public void setup() {
        guava = new GuavaOperations();
        SequenceBenchmarkUtils.assertZipTopArtistAndTrackByCountryBenchmarkValidity(guava);
    }

    // Stream Based benchmarks

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void protonpack(Blackhole bh) {
        ProtonpackOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void guava(Blackhole bh) {
        guava.zipTopArtistAndTrackByCountry(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks()
        ).forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void zipline(Blackhole bh) {
        ZiplineOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        StreamExOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        QueryOperations.zipTopArtistAndTrackByCountry().traverse(bh::consume);
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        JoolOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        VavrOperations.zipTopArtistAndTrackByCountry().forEach(bh::consume);
    }

}
