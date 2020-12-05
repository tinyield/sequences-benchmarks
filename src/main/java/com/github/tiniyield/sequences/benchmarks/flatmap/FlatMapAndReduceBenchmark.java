package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.tinyield.Sek;
import kotlin.collections.CollectionsKt;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.kt.flatmap.FlatmapAndReduceKt.flatMapAndReduce;
import static kotlin.sequences.SequencesKt.flatMap;
import static kotlin.sequences.SequencesKt.reduce;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FlatMapAndReduceBenchmark {


    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"10000"})
    public int COLLECTION_SIZE;

    /**
     * The data source used to benchmark
     * This data is instantiated using the getNestedList() method.
     */
    private List<List<Integer>> data;

    /**
     * Creates a nested List of a single Integer each
     *
     * @return the nested List
     */
    public List<List<Integer>> getNestedList() {
        List<List<Integer>> result = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            result.add(List.of(1));
        }
        return result;
    }

    /**
     * Creates a nested sequence mapped by two mappers, one for the inner sequence and one for the outer sequence
     *
     * @param innerMapper the mapper to create the inner sequence out of a List of Integers
     * @param outerMapper the mapper to create the outer sequence out of a List of inner sequences
     * @return the nested sequence of the desired type
     */
    public <T, U> T getNestedSequence(Function<List<Integer>, U> innerMapper, Function<List<U>, T> outerMapper) {
        List<U> result = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            result.add(innerMapper.apply(data.get(i)));
        }
        return outerMapper.apply(result);
    }


    /**
     * Sets up the data source to be used in this benchmark
     */
    @Setup
    public void setup() {
        data = getNestedList();
    }

    /**
     * Maps the nested {@link Stream} sequence into an {@link Integer} {@link Stream} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */

    @Benchmark
    public Integer stream() {
        return getNestedSequence(List::stream, List::stream)
                .flatMap(i -> i)
                .reduce(Integer::sum)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link StreamEx} sequence into an {@link Integer} {@link StreamEx} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer streamEx() {
        return getNestedSequence(StreamEx::of, StreamEx::of)
                .flatMap(i -> i)
                .reduce(Integer::sum)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Query} sequence into an {@link Integer} {@link Query} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer jayield() {
        return getNestedSequence(Query::fromList, Query::fromList)
                .flatMap(i -> i)
                .reduce(Integer::sum)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Seq} sequence into an {@link Integer} {@link Seq} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer jool() {
        return getNestedSequence(Seq::seq, Seq::seq)
                .flatMap(i -> i)
                .reduce(Integer::sum)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link io.vavr.collection.Stream} sequence into an {@link Integer}
     * {@link io.vavr.collection.Stream} and reduces it by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer vavr() {
        return getNestedSequence(io.vavr.collection.Stream::ofAll, io.vavr.collection.Stream::ofAll)
                .flatMap(i -> i)
                .reduce(Integer::sum);
    }

    /**
     * Maps the nested Kotlin {@link Sequence} into an {@link Integer} Kotlin {@link Sequence}
     * and reduces it by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer kotlin() {
        return flatMapAndReduce(getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence));
    }

    /**
     * Maps the nested Kotlin {@link Sequence} in Java into an {@link Integer} Kotlin {@link Sequence} in Java
     * and reduces it by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer jkotlin() {
        return reduce(
                flatMap(
                        getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence),
                        i -> i
                ),
                Integer::sum
        );
    }

    /**
     * Maps the nested {@link LazyIterable} sequence into an {@link Integer} {@link LazyIterable} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer eclipse() {
        return getNestedSequence(n -> Lists.immutable.ofAll(n).asLazy(), n -> Lists.immutable.ofAll(n).asLazy())
                .flatCollect(i -> i)
                .reduce(Integer::sum)
                .orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Sek} sequence into an {@link Integer} {@link Sek} and reduces it
     * by summing all values.
     *
     * @return the sum of all values
     */
    @Benchmark
    public Integer sek() {
        return getNestedSequence(Sek::of, Sek::of)
                .flatMap(i -> i)
                .reduceOrNull(Integer::sum);
    }

}
