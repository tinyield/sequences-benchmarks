package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.kt.all.match.IsEveryEvenKt;
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
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.all;

/**
 * AllMatchBenchmark
 * Benchmarks the `allMatch()` operation in the different sequence types.
 *
 * Pipeline:
 * Sequence.of(new Integer[]{ EVEN, EVEN, ..., EVEN })
 *              .allMatch(isEven)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class AllMatchBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"10000"})
    public int COLLECTION_SIZE;

    /**
     * The data source used to benchmark
     * This data is instantiated using the getAllEvenArray method.
     */
    public Integer[] data;

    /**
     * Sets up the data source to be used in this benchmark
     */
    @Setup
    public void setup() {
        data = getAllEvenArray();
    }

    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        bh.consume(isEveryEven(Arrays.stream(data)));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(isEveryEven(StreamEx.of(data)));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(isEveryEven(Query.of(data)));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(isEveryEven(Seq.of(data)));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(isEveryEven(io.vavr.collection.Stream.of(data)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(IsEveryEvenKt.isEveryEven(asSequence(data)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(isEveryEven(asSequence(data)));
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Stream}s
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(Stream<Integer> numbers) {
        return numbers.allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link StreamEx}s
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(StreamEx<Integer> numbers) {
        return numbers.allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Query}s
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(Query<Integer> numbers) {
        return numbers.allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Seq}s
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(Seq<Integer> numbers) {
        return numbers.allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link io.vavr.collection.Stream}s
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(io.vavr.collection.Stream<Integer> numbers) {
        return numbers.forAll(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using Kotlin {@link Sequence}s in Java
     * @return whether every Integer is even or not
     */
    public boolean isEveryEven(Sequence<Integer> numbers) {
        return all(numbers, AllMatchBenchmark::isEven);
    }

    /**
     * Checks wether or not an Integer is an even value
     * @param value the Integer to check
     * @return true if the Integer is even, false otherwise
     */
    public static boolean isEven(Integer value) {
        return value % 2 == 0;
    }

    /**
     * Gets an Integer[] filled with the constant EVEN and of size COLLECTION_SIZE
     * @return an Integer[] of size COLLECTION_SIZE filled with the constant EVEN
     */
    public Integer[] getAllEvenArray() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

}
