package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertEveryEvenValidity;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchBenchmark extends AbstractSequenceOperationsBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    private Integer[] data;

    public Integer[] getAllEvenArray() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

    @Setup
    public void setup() {
        super.init();
        data = getAllEvenArray();
        assertEveryEvenValidity(getStream(), getStreamEx(), getQuery(), getJool(), getVavr());
    }

    private boolean getStream() {
        return stream.isEveryEven(Arrays.stream(data));
    }

    private boolean getStreamEx() {
        return streamEx.isEveryEven(StreamEx.of(data));
    }

    private boolean getQuery() {
        return query.isEveryEven(Query.of(data));
    }

    private boolean getJool() {
        return jool.isEveryEven(Seq.of(data));
    }

    private boolean getVavr() {
        return vavr.isEveryEven(io.vavr.collection.Stream.of(data));
    }

    private boolean getKotlin() {
        return kotlin.isEveryEven(ArraysKt.asSequence(data));
    }

    private boolean getJKotlin() {
        return jkotlin.isEveryEven(ArraysKt.asSequence(data));
    }

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(getStream());
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(getStreamEx());
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(getQuery());
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(getJool());
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(getVavr());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(getKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(getJKotlin());
    }

}
