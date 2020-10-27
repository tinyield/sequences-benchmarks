package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.kt.all.match.IsEveryEvenKt;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import kotlin.collections.ArraysKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertEveryEvenValidity;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    private Integer[] data;

    public Integer[] getAllEvenArray() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

    public boolean isEveryEven(Stream<Integer> numbers) {
        return numbers.allMatch(SequenceBenchmarkUtils::isEven);
    }

    public boolean isEveryEven(StreamEx<Integer> numbers) {
        return numbers.allMatch(SequenceBenchmarkUtils::isEven);
    }

    public boolean isEveryEven(Query<Integer> numbers) {
        return numbers.allMatch(SequenceBenchmarkUtils::isEven);
    }

    public boolean isEveryEven(Seq<Integer> numbers) {
        return numbers.allMatch(SequenceBenchmarkUtils::isEven);
    }

    public boolean isEveryEven(io.vavr.collection.Stream<Integer> numbers) {
        return numbers.forAll(SequenceBenchmarkUtils::isEven);
    }

    public boolean isEveryEven(Sequence<Integer> numbers) {
        return SequencesKt.all(numbers, SequenceBenchmarkUtils::isEven);
    }

    @Setup
    public void setup() {
        data = getAllEvenArray();
        assertEveryEvenValidity(isEveryEvenStream(), isEveryEvenStreamEx(), isEveryEvenQuery(), isEveryEvenJool(), isEveryEvenVavr());
    }

    public boolean isEveryEvenStream() {
        return isEveryEven(Arrays.stream(data));
    }

    public boolean isEveryEvenStreamEx() {
        return isEveryEven(StreamEx.of(data));
    }

    public boolean isEveryEvenQuery() {
        return isEveryEven(Query.of(data));
    }

    public boolean isEveryEvenJool() {
        return isEveryEven(Seq.of(data));
    }

    public boolean isEveryEvenVavr() {
        return isEveryEven(io.vavr.collection.Stream.of(data));
    }

    public boolean isEveryEvenKotlin() {
        return IsEveryEvenKt.isEveryEven(ArraysKt.asSequence(data));
    }

    public boolean isEveryEvenJKotlin() {
        return isEveryEven(ArraysKt.asSequence(data));
    }

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(isEveryEvenStream());
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(isEveryEvenStreamEx());
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(isEveryEvenQuery());
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(isEveryEvenJool());
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(isEveryEvenVavr());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(isEveryEvenKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(isEveryEvenJKotlin());
    }

}
