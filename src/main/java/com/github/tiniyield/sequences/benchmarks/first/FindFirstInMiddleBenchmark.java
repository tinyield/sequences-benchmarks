package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.kt.first.FirstKt;
import kotlin.sequences.Sequence;
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

import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.ODD;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.firstOrNull;

/**
 * FindFirstInMiddleBenchmark
 * Benchmarks the usage of the `findFirst()` operator.
 * The match element is found in the middle of the sequence.
 *
 * Pipeline:
 * Sequence.of(new Integer[]{ EVEN, EVEN,...,ODD, ..., EVEN })
 * .filter(isOdd)
 * .findFirst()
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInMiddleBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"10000"})
    public int COLLECTION_SIZE;

    /**
     * The data source used to benchmark
     * This data is instantiated using the get method.
     */
    public Integer[] data;

    /**
     * Prepares an array with a single ODD element in the middle.
     * @return an array of Integers
     */
    public Integer[] get() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        numbers[(COLLECTION_SIZE / 2) - 1] = ODD;
        return numbers;
    }

    /**
     * Sets up the data source to be used in this benchmark
     */
    @Setup
    public void init() {
        data = get();
    }

    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(findFirst(Arrays.stream(data)));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(findFirst(StreamEx.of(data)));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(findFirst(Query.of(data)));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(findFirst(Seq.of(data)));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(findFirst(io.vavr.collection.Stream.of(data)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(FirstKt.findFirst(asSequence(data)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(findFirst(asSequence(data)));
    }


    /**
     * Searches a {@link Stream} sequence for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(Stream<Integer> numbers) {
        return numbers.filter(IsOdd::isOdd).findFirst().orElse(null);
    }

    /**
     * Searches a {@link StreamEx} sequence for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(StreamEx<Integer> numbers) {
        return numbers.filter(IsOdd::isOdd).findFirst().orElse(null);
    }


    /**
     * Searches a  {@link Query} sequence for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(Query<Integer> numbers) {
        return numbers.filter(IsOdd::isOdd).findFirst().orElse(null);
    }

    /**
     * Searches a {@link Seq} sequence for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(Seq<Integer> numbers) {
        return numbers.filter(IsOdd::isOdd).findFirst().orElse(null);
    }

    /**
     * Searches a {@link io.vavr.collection.Stream} sequence for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(io.vavr.collection.Stream<Integer> numbers) {
        return numbers.find(IsOdd::isOdd).getOrNull();
    }

    /**
     * Searches a Kotlin {@link Sequence} in Java for an odd number
     * @param numbers the sequence to search in
     * @return the first odd Integer in the sequence or null if none exists
     */
    public Integer findFirst(Sequence<Integer> numbers) {
        return firstOrNull(filter(numbers, IsOdd::isOdd));
    }
}
