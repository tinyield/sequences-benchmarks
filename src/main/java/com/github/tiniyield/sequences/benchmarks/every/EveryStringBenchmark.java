package com.github.tiniyield.sequences.benchmarks.every;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.every.EveryKt;
import com.google.common.collect.Streams;
import com.tinyield.Sek;
import io.vavr.collection.Stream;
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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tiniyield.sequences.benchmarks.zip.StreamZipOperation.zip;
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
     * Zips two sequences of {@link java.util.stream.Stream} together mapping the combination of each value to a boolean
     * with the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean stream() {
        return zip(lstA.stream(), lstB.stream(), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link StreamEx} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean streamEx() {
        return StreamEx.of(lstA)
                .zipWith(StreamEx.of(lstB), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Query} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean jayield() {
        return Query.fromList(lstA)
                .zip(Query.fromList(lstB), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Seq} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean jool() {
        return Seq.seq(lstA)
                .zip(Seq.seq(lstB), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link io.vavr.collection.Stream} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean vavr() {
        return Stream.ofAll(lstA)
                .zipWith(Stream.ofAll(lstB), String::equals)
                .forAll(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together mapping the combination of each value to a boolean with
     * the BiPredicate, making use of Protonpack.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean protonpack() {
        return StreamUtils.zip(lstA.stream(), lstB.stream(), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Stream} together mapping the combination of each value to a boolean with
     * the BiPredicate, making use of Guava.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean guava() {
        return Streams.zip(lstA.stream(), lstB.stream(), String::equals)
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Stream} together mapping the combination of each value to a boolean with
     * the BiPredicate, using the zipline approach.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean zipline() {
        Iterator<String> it = lstB.stream().iterator();
        return lstA.stream().map(t -> t.equals(it.next()))
                .allMatch(Boolean.TRUE::equals);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     */
    @Benchmark
    public boolean kotlin() {
        return EveryKt.every(asSequence(lstA), asSequence(lstB), String::equals);
    }

    /**
     * Zips two sequences of Kotlin {@link Sequence}s in Java together mapping the combination of each String to a boolean with
     * the BiPredicate.
     *
     * @return true if all Strings in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean jkotlin() {
        return all(zip(asSequence(lstA), asSequence(lstB), String::equals), Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link LazyIterable} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public boolean eclipse() {
        return Lists.immutable.ofAll(lstA).asLazy()
                .zip(Lists.immutable.ofAll(lstB).asLazy())
                .collect(p -> p.getOne().equals(p.getTwo()))
                .allSatisfy(Boolean.TRUE::equals);
    }

    /**
     * Zips two sequences of {@link Sek} together mapping the combination of each value to a boolean with
     * the BiPredicate.
     *
     * @return true if all values in the zipped sequence are true, false otherwise.
     */
    @Benchmark
    public final boolean sek() {
        return Sek.of(lstA)
                .zip(Sek.of(lstB), String::equals)
                .all(Boolean.TRUE::equals);
    }

}
