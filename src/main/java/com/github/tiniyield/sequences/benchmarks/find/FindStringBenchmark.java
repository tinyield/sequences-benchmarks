package com.github.tiniyield.sequences.benchmarks.find;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.find.FindKt;
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
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
 * <p>
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
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String stream() {
        return zip(lstA.stream(), lstB.stream(), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link StreamEx} together, using the BiPredicate to let through an element if a match is
     * made or null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String streamEx() {
        return StreamEx.of(lstA)
                .zipWith(StreamEx.of(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Query} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String jayield() {
        return Query.fromList(lstA)
                .zip(Query.fromList(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Seq} together, using the BiPredicate to let through an element if a match is made or
     * null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String jool() {
        return Seq.seq(lstA)
                .zip(Seq.seq(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link io.vavr.collection.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise.
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String vavr() {
        return Stream.ofAll(lstA)
                .zipWith(Stream.ofAll(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .getOrNull();
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise, making use of Protonpack.
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String protonpack() {
        return StreamUtils.zip(lstA.stream(), lstB.stream(), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, making use of Guava.
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String guava() {
        return Streams.zip(lstA.stream(), lstB.stream(), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, using the zipline approach.
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String zipline() {
        Iterator<String> it = lstB.stream().iterator();
        return lstA.stream().map(t -> t.equals(it.next()) ? t : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     */
    @Benchmark
    public String kotlin() {
        return FindKt.find(asSequence(lstA), asSequence(lstB), String::equals);
    }

    /**
     * Zips two sequences of Kotlin {@link Sequence}s in Java together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String jkotlin() {
        return firstOrNull(
                filter(
                        zip(asSequence(lstA), asSequence(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }

    /**
     * Zips two sequences of {@link LazyIterable} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise.
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public String eclipse() {
        return Lists.immutable.ofAll(lstA).asLazy()
                .zip(Lists.immutable.ofAll(lstB).asLazy())
                .collect(p -> p.getOne().equals(p.getTwo()) ? p.getOne() : null)
                .select(Objects::nonNull)
                .getFirst();
    }


    /**
     * Zips two {@link Sek}s together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     *
     * @return the found String if a match was found, null otherwise.
     */
    @Benchmark
    public final String sek() {
        return Sek.of(lstA)
                .zip(Sek.of(lstB), (t1, t2) -> t1.equals(t2) ? t1 : null)
                .filter(Objects::nonNull)
                .firstOrNull();
    }
}
