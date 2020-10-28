package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.kt.first.FirstKt;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import kotlin.collections.ArraysKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.ODD;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInBeginningBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    public Integer[] data;

    public Integer[] get() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            numbers[i] = i;
        }
        numbers[0] = ODD;
        return numbers;
    }

    @Setup
    public void init() {
        data = get();
    }

    public Integer findFirst(Seq<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst().orElse(null);
    }

    public Integer findFirst(Stream<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst().orElse(null);
    }

    public Integer findFirst(StreamEx<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst().orElse(null);
    }

    public Integer findFirst(Query<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst().orElse(null);
    }

    public Integer findFirst(io.vavr.collection.Stream<Integer> numbers) {
        return numbers.find(SequenceBenchmarkUtils::isOdd).getOrNull();
    }

    public Integer findFirst(Sequence<Integer> numbers) {
        return SequencesKt.firstOrNull(SequencesKt.filter(numbers, SequenceBenchmarkUtils::isOdd));
    }

    public Integer findFirstInJool() {
        return findFirst(Seq.of(data));
    }

    public Integer findFirstInStream() {
        return findFirst(Arrays.stream(data));
    }

    public Integer findFirstInStreamEx() {
        return findFirst(StreamEx.of(data));
    }

    public Integer findFirstInQuery() {
        return findFirst(Query.of(data));
    }

    public Integer findFirstInVavr() {
        return findFirst(io.vavr.collection.Stream.of(data));
    }

    public Integer findFirstInKotlin() {
        return FirstKt.findFirst(ArraysKt.asSequence(data));
    }

    public Integer findFirstInJKotlin() {
        return findFirst(ArraysKt.asSequence(data));
    }

    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(findFirstInStream());
    }

    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(findFirstInStreamEx());
    }

    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(findFirstInQuery());
    }

    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(findFirstInJool());
    }

    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(findFirstInVavr());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(findFirstInKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(findFirstInJKotlin());
    }


}
