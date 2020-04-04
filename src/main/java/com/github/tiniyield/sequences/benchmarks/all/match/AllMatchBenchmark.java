package com.github.tiniyield.sequences.benchmarks.all.match;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertEveryEvenValidity;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initEvenDataProvider;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.ISequenceBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchBenchmark implements ISequenceBenchmark {

    @Param({"10000"})
    private int COLLECTION_SIZE;

    private JoolOperations jool;
    private QueryOperations query;
    private StreamExOperations streamEx;
    protected StreamOperations stream;

    @Setup
    public void setup() {
        initEvenDataProvider(COLLECTION_SIZE);
        jool = new JoolOperations();
        query = new QueryOperations();
        streamEx = new StreamExOperations();
        stream = new StreamOperations();
        assertEveryEvenValidity(getStream(), getStreamEx(), getQuery(), getJool(), getVavr());
    }

    private boolean getStream() {
        return stream.isEveryEven(getEvenDataProvider().asStream());
    }

    private boolean getStreamEx() {
        return streamEx.isEveryEven(getEvenDataProvider().asStreamEx());
    }

    private boolean getQuery() {
        return query.isEveryEven(getEvenDataProvider().asQuery());
    }

    private boolean getJool() {
        return jool.isEveryEven(getEvenDataProvider().asSeq());
    }

    private boolean getVavr() {
        return VavrOperations.everyEven();
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(getStreamEx());
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(getQuery());
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(getJool());
    }

    @Override
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(getVavr());
    }

}
