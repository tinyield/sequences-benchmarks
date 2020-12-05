package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.kt.first.FirstKt;
import com.tinyield.Sek;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.impl.factory.Lists;
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
import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.ODD;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.firstOrNull;

/**
 * FindFirstInBeginningBenchmark
 * Benchmarks the usage of the `findFirst()` operator.
 * The match element is found in the first element.
 * <p>
 * Pipeline:
 * Sequence.of(new Integer[]{ ODD, EVEN, ..., EVEN })
 * .filter(isOdd)
 * .findFirst()
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFirstInBeginningBenchmark {

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
     * Prepares an array with a single ODD element in the beginning.
     *
     * @return an array of Integers
     */
    public Integer[] get() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        Arrays.fill(numbers, EVEN);
        numbers[0] = ODD;
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
     * Searches a {@link Stream} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer stream() {
        return Arrays.stream(data)
                .filter(IsOdd::isOdd)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches a {@link StreamEx} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer streamEx() {
        return StreamEx.of(data)
                .filter(IsOdd::isOdd)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches a  {@link Query} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer jayield() {
        return Query.of(data)
                .filter(IsOdd::isOdd)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches a {@link Seq} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer jool() {
        return Seq.of(data)
                .filter(IsOdd::isOdd)
                .findFirst()
                .orElse(null);
    }

    /**
     * Searches a {@link io.vavr.collection.Stream} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer vavr() {
        return io.vavr.collection.Stream.of(data)
                .find(IsOdd::isOdd)
                .getOrNull();
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     */
    @Benchmark
    public final Integer kotlin() {
        return FirstKt.findFirst(asSequence(data));
    }

    /**
     * Searches a Kotlin {@link Sequence} in Java for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer jkotlin() {
        return firstOrNull(filter(asSequence(data), IsOdd::isOdd));
    }

    /**
     * Searches a  {@link LazyIterable} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public final Integer eclipse() {
        return Lists.immutable.of(data).asLazy()
                .select(IsOdd::isOdd)
                .getFirst();
    }

    /**
     * Searches a  {@link Sek} sequence for an odd number
     *
     * @return the first odd Integer in the sequence or null if none exists
     */
    @Benchmark
    public Integer sek() {
        return Sek.of(data)
                .filter(IsOdd::isOdd)
                .firstOrNull();
    }

}
