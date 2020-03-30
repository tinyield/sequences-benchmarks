package com.github.tiniyield.sequences.benchmarks;

import org.openjdk.jmh.infra.Blackhole;

public interface ISequenceBenchmark {
    void stream(Blackhole bh);
    void streamEx(Blackhole bh);
    void jayield(Blackhole bh);
    void jool(Blackhole bh);
    void vavr(Blackhole bh);
}
