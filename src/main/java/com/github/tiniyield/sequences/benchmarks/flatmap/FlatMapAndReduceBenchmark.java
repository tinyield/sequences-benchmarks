package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.github.tiniyield.sequences.benchmarks.kt.flatmap.FlatmapAndReduceKt;
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
import org.openjdk.jmh.infra.Blackhole;
import com.tinyield.Sek;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

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
     * Sets up the data source to be used in this benchmark
     */
    @Setup
    public void setup() {
        data = getNestedList();
    }

    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(flatMapAndReduceStream(getNestedSequence(List::stream, List::stream)));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(flatMapAndReduceStreamEx(getNestedSequence(StreamEx::of, StreamEx::of)));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(flatMapAndReduceQuery(getNestedSequence(Query::fromList, Query::fromList)));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(flatMapAndReduceJool(getNestedSequence(Seq::seq, Seq::seq)));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(flatMapAndReduceVavr(getNestedSequence(io.vavr.collection.Stream::ofAll, io.vavr.collection.Stream::ofAll)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(FlatmapAndReduceKt.flatMapAndReduce(getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence)));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(flatMapAndReduceJKotlin(getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence)));
    }

    /**
     * Runs this benchmark using {@link LazyIterable}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void eclipse(Blackhole bh) {
        bh.consume(flatMapAndReduceEclipse(getNestedSequence(n -> Lists.immutable.ofAll(n).asLazy(), n -> Lists.immutable.ofAll(n).asLazy())));
    }

    /**
     * Runs this benchmark using {@link Sek}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void sek(Blackhole bh) {
        bh.consume(flatMapAndReduceSek(getNestedSequence(Sek::of, Sek::of)));
    }

    /**
     * Maps the nested {@link Stream} sequence into an {@link Integer} {@link Stream} and reduces it
     * by summing all values.
     *
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceStream(Stream<Stream<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link StreamEx} sequence into an {@link Integer} {@link StreamEx} and reduces it
     * by summing all values.
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceStreamEx(StreamEx<StreamEx<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Query} sequence into an {@link Integer} {@link Query} and reduces it
     * by summing all values.
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceQuery(Query<Query<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Seq} sequence into an {@link Integer} {@link Seq} and reduces it
     * by summing all values.
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceJool(Seq<Seq<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link io.vavr.collection.Stream} sequence into an {@link Integer}
     * {@link io.vavr.collection.Stream} and reduces it by summing all values.
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceVavr(io.vavr.collection.Stream<io.vavr.collection.Stream<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum);
    }

    /**
     * Maps the nested Kotlin {@link Sequence} in Java into an {@link Integer} Kotlin {@link Sequence} in Java
     * and reduces it by summing all values.
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceJKotlin(Sequence<Sequence<Integer>> input) {
        return reduce(
                flatMap(
                        input,
                        i -> i
                ),
                Integer::sum
        );
    }

    /**
     * Maps the nested {@link LazyIterable} sequence into an {@link Integer} {@link LazyIterable} and reduces it
     * by summing all values.
     *
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceEclipse(LazyIterable<LazyIterable<Integer>> input) {
        return input.flatCollect(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    /**
     * Maps the nested {@link Sek} sequence into an {@link Integer} {@link Sek} and reduces it
     * by summing all values.
     *
     * @param input the nested sequence
     * @return the sum of all values
     */
    public Integer flatMapAndReduceSek(Sek<Sek<Integer>> input) {
        return input.flatMap(i -> i).reduceOrNull(Integer::sum);
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

}
