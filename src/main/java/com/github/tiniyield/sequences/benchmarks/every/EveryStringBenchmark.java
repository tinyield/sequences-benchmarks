package com.github.tiniyield.sequences.benchmarks.every;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.every.EveryKt;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tiniyield.sequences.benchmarks.operations.CustomStreamOperations.zip;
import static kotlin.collections.CollectionsKt.asSequence;
import static kotlin.sequences.SequencesKt.all;
import static kotlin.sequences.SequencesKt.zip;

/**
 * EveryStringBenchmark
 * Every is an operation that, based on a user defined predicate, tests if all the
 * elements of a sequence match between corresponding positions.
 * <p>
 * Pipeline:
 * Sequence.of(new String[]{"1", "2", ..., "..."})
 * .zip(Sequence.of(new String[]{"1", "2", ..., "..."}), String::Equals)
 * .allMatch(Boolean.TRUE::equals)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class EveryStringBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"100"})
    public int COLLECTION_SIZE;

    /**
     * lstA and lstB are two Lists with the same String  objects.
     */
    public List<String> lstA;
    public List<String> lstB;

    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        lstB = new ArrayList<>(COLLECTION_SIZE);
        lstA = IntStream
                .rangeClosed(1, COLLECTION_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        lstB.addAll(lstA);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void stream(Blackhole bh) {
        bh.consume(every(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(every(StreamEx.of(lstA), StreamEx.of(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(every(Query.fromList(lstA), Query.fromList(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(every(Seq.seq(lstA), Seq.seq(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(every(Stream.ofAll(lstA), Stream.ofAll(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void protonpack(Blackhole bh) {
        bh.consume(everyProtonpack(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void guava(Blackhole bh) {
        bh.consume(everyGuava(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void zipline(Blackhole bh) {
        bh.consume(everyZipline(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(EveryKt.every(asSequence(lstA), asSequence(lstB), String::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(every(asSequence(lstA), asSequence(lstB), String::equals));
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together mapping the combination of each String to a boolean
     * with the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        return zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link StreamEx} together mapping the combination of each String to a boolean with
     * the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(StreamEx<T> q1, StreamEx<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Query} together mapping the combination of each String to a boolean with
     * the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(Query<T> q1, Query<U> q2, BiPredicate<T, U> predicate) {
        return q1.zip(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together mapping the combination of each String to a boolean with
     * the BiPredicate, making use of Protonpack.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean everyProtonpack(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        return StreamUtils.zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Seq} together mapping the combination of each String to a boolean with
     * the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(Seq<T> q1, Seq<U> q2, BiPredicate<T, U> predicate) {
        return q1.zip(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link io.vavr.collection.Stream} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(Stream<T> q1, Stream<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test).forAll(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Stream} together mapping the combination of each String to a boolean with
     * the BiPredicate, making use of Guava.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean everyGuava(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        return Streams.zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Stream} together mapping the combination of each String to a boolean with
     * the BiPredicate, using the zipline approach.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean everyZipline(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        Iterator<U> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next())).allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of Kotlin {@link Sequence}s in Java together mapping the combination of each String to a boolean with
     * the BiPredicate.
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    public <T, U> boolean every(Sequence<T> q1, Sequence<U> q2, BiPredicate<T, U> predicate) {
        return all(zip(q1, q2, predicate::test), Boolean.TRUE::equals);
    }

}
