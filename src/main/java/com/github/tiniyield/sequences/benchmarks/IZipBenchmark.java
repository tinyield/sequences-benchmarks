package com.github.tiniyield.sequences.benchmarks;

import org.openjdk.jmh.infra.Blackhole;

public interface IZipBenchmark extends ISequenceBenchmark {
    void protonpack(Blackhole bh);
    void guava(Blackhole bh);
    void zipline(Blackhole bh);
}
