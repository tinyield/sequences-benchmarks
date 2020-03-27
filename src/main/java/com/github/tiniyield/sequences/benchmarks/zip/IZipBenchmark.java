package com.github.tiniyield.sequences.benchmarks.zip;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.GuavaBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.JoolBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.ProtonpackBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.QueryBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.StreamBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.StreamExBenchmark;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations.ZiplineBenchmark;

public interface IZipBenchmark {
    void stream(Blackhole bh);
    void protonpack(Blackhole bh);
    void guava(Blackhole bh);
    void zipline(Blackhole bh);
    void streamEx(Blackhole bh);
    void jayield(Blackhole bh);
    void jool(Blackhole bh);
}
