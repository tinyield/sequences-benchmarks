package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.kt.all.match.IsEveryEvenKt;
import com.tinyield.Sek;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.factory.Lists;
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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.EVEN;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.all;

/**
 * AllMatchBenchmark
 * Benchmarks the `allMatch()` operation in the different sequence types.
 * <p>
 * Pipeline:
 * Sequence.of(new Integer[]{ EVEN, EVEN, ..., EVEN })
 * .allMatch(isEven)
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
     * Checks wether or not an Integer is an even value
     *
     * @param value the Integer to check
     * @return true if the Integer is even, false otherwise
     */
    public static final boolean isEven(Integer value) {
        return value % 2 == 0;
    }

    /**
     * Gets an Integer[] filled with the constant EVEN and of size COLLECTION_SIZE
     *
     * @return an Integer[] of size COLLECTION_SIZE filled with the constant EVEN
     */
    public Integer[] getAllEvenArray() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

    /**
     * Sets up the data source to be used in this benchmark
     */
    @Setup
    public void setup() {
        data = getAllEvenArray();
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Stream}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean stream() {
        return Arrays.stream(data).allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link StreamEx}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean streamEx() {
        return StreamEx.of(data).allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Query}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean jayield() {
        return Query.of(data).allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Seq}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean jool() {
        return Seq.of(data).allMatch(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link io.vavr.collection.Stream}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean vavr() {
        return io.vavr.collection.Stream.of(data).forAll(AllMatchBenchmark::isEven);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     */
    @Benchmark
    public boolean kotlin() {
        return IsEveryEvenKt.isEveryEven(asSequence(data));
    }

    /**
     * Checks if every Integer in a sequence is Even, using Kotlin {@link Sequence}s in Java
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean jkotlin() {
        return all(asSequence(data), AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link Sek}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean sek() {
        return Sek.of(data).all(AllMatchBenchmark::isEven);
    }

    /**
     * Checks if every Integer in a sequence is Even, using {@link LazyIterable}s
     *
     * @return whether every Integer is even or not
     */
    @Benchmark
    public boolean eclipse() {
        return Lists.mutable.with(data).asLazy().allSatisfy(AllMatchBenchmark::isEven);
    }

}
