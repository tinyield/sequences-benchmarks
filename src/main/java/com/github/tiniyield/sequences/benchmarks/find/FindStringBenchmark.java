package com.github.tiniyield.sequences.benchmarks.find;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.find.FindKt;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import com.tinyield.Sek;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tiniyield.sequences.benchmarks.zip.StreamZipOperation.zip;
import static kotlin.collections.CollectionsKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.firstOrNull;
import static kotlin.sequences.SequencesKt.zip;

/**
 * FindStringBenchmark
 * The `find` between two sequences is an operation that, based on a user defined
 * predicate, finds two elements that match, returning one of them in the process.
 *
 * Pipeline:
 * Sequence.of(new String[]{"1", "2",...})
 * .zip(Sequence.of(new String[]{"1", "2",...}), String::Equals)
 * .filter(Objects::nonNull)
 * .findFirst()
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindStringBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"1000"})
    public int COLLECTION_SIZE;

    /**
     * The current match index, is updated per iteration
     */
    public int index;

    /**
     * A list containing Strings initiated with integers from 0 to COLLECTION_SIZE
     */
    public List<String> lstA;
    /**
     * A list containing Strings initiated with -1
     */
    public List<String> lstB;

    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        index = 0;
        lstA = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        lstB = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .map(v -> -1)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Updates the match index with each Invocation
     */
    @Setup(Level.Invocation)
    public void update() {
        index++;
        lstB.set((index - 1) % COLLECTION_SIZE, String.valueOf(-1));
        lstB.set(index % COLLECTION_SIZE, String.valueOf(index % COLLECTION_SIZE));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(find(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(find(StreamEx.of(lstA), StreamEx.of(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(find(Query.fromList(lstA), Query.fromList(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(find(Seq.seq(lstA), Seq.seq(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(find(Stream.ofAll(lstA), Stream.ofAll(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void protonpack(Blackhole bh) {
        bh.consume(findInProtonpack(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void guava(Blackhole bh) {
        bh.consume(findInGuava(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void zipline(Blackhole bh) {
        bh.consume(findInZipline(lstA.stream(), lstB.stream(), String::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(FindKt.find(asSequence(lstA), asSequence(lstB), String::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(find(asSequence(lstA), asSequence(lstB), String::equals));
    }

    /**
     * Runs this benchmark using {@link LazyIterable}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void eclipse(Blackhole bh) {
        bh.consume(find(Lists.immutable.ofAll(lstA).asLazy(), Lists.immutable.ofAll(lstB).asLazy(), String::equals));
    }

    /**
     * Runs this benchmark using {@link Sek}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void sek(Blackhole bh) {
        bh.consume(find(Sek.of(lstA), Sek.of(lstB), String::equals));
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link StreamEx} together, using the BiPredicate to let through an element if a match is
     * made or null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(StreamEx<String> q1, StreamEx<String> q2, BiPredicate<String, String> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Query} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(Query<String> q1, Query<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Seq} together, using the BiPredicate to let through an element if a match is made or
     * null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(Seq<String> q1, Seq<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link io.vavr.collection.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise.
     * @return the found String if a match was found, null otherwise.
     */
    public String find(Stream<String> q1, Stream<String> q2, BiPredicate<String, String> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull).getOrNull();
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise, making use of Protonpack.
     * @return the found String if a match was found, null otherwise.
     */
    public String findInProtonpack(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return StreamUtils.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, making use of Guava.
     * @return the found String if a match was found, null otherwise.
     */
    public String findInGuava(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return Streams.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, using the zipline approach.
     * @return the found String if a match was found, null otherwise.
     */
    public String findInZipline(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        Iterator<String> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next()) ? t : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of Kotlin {@link Sequence}s in Java together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(Sequence<String> q1, Sequence<String> q2, BiPredicate<String, String> predicate) {
        return firstOrNull(
                filter(
                        zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }

    /**
     * Zips two sequences of {@link LazyIterable} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise.
     * @return the found String if a match was found, null otherwise.
     */
    public String find(LazyIterable<String> q1, LazyIterable<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2)
                .collect((p) -> predicate.test(p.getOne(), p.getTwo()) ? p.getOne() : null)
                .select(Objects::nonNull)
                .getFirst();
    }

    /**
     * Zips two {@link Sek}s together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     * @return the found String if a match was found, null otherwise.
     */
    public String find(Sek<String> q1, Sek<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .firstOrNull();
    }
}
