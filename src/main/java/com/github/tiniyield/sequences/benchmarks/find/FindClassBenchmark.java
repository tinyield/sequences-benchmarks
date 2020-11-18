/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.tiniyield.sequences.benchmarks.find;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.find.FindKt;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
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
 * FindClassBenchmark
 * The `find` between two sequences is an operation that, based on a user defined
 * predicate, finds two elements that match, returning one of them in the process.
 *
 * Pipeline:
 * Sequence.of(new Value[]{new Value(1), new Value(2),..., new Value(...)})
 * .zip(Sequence.of(new Value[]{new Value(1), new Value(2),..., new Value(...)}), Value::Equals)
 * .filter(Objects::nonNull)
 * .findFirst()
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindClassBenchmark {

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
     * A list containing Values initiated with integers from 0 to COLLECTION_SIZE
     */
    public List<Value> lstA;

    /**
     * A list containing Values initiated with -1
     */
    public List<Value> lstB;

    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        index = 0;
        lstA = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .mapToObj(Value::new)
                .collect(Collectors.toList());
        lstB = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .map(v -> -1)
                .mapToObj(Value::new)
                .collect(Collectors.toList());
    }

    /**
     * Updates the match index with each Invocation
     */
    @Setup(Level.Invocation)
    public void update() {
        index++;
        lstB.set((index - 1) % COLLECTION_SIZE, new Value(-1));
        lstB.set(index % COLLECTION_SIZE, new Value(index % COLLECTION_SIZE));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(find(lstA.stream(), lstB.stream(), Value::equals));
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(find(StreamEx.of(lstA), StreamEx.of(lstB), Value::equals));
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(find(Query.fromList(lstA), Query.fromList(lstB), Value::equals));
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(find(Seq.seq(lstA), Seq.seq(lstB), Value::equals));
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(find(Stream.ofAll(lstA), Stream.ofAll(lstB), Value::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void protonpack(Blackhole bh) {
        bh.consume(findInProtonpack(lstA.stream(), lstB.stream(), Value::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void guava(Blackhole bh) {
        bh.consume(findInGuava(lstA.stream(), lstB.stream(), Value::equals));
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void zipline(Blackhole bh) {
        bh.consume(findInZipline(lstA.stream(), lstB.stream(), Value::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(FindKt.find(asSequence(lstA), asSequence(lstB), Value::equals));
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(find(asSequence(lstA), asSequence(lstB), Value::equals));
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link StreamEx} together, using the BiPredicate to let through an element if a match is
     * made or null otherwise
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(StreamEx<Value> q1, StreamEx<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Query} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(Query<Value> q1, Query<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Seq} together, using the BiPredicate to let through an element if a match is made or
     * null otherwise
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(Seq<Value> q1, Seq<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link io.vavr.collection.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise.
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(Stream<Value> q1, Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull).getOrNull();
    }

    /**
     * Zips two sequences of {@link java.util.stream.Stream} together, using the BiPredicate to let through an element
     * if a match is made or null otherwise, making use of Protonpack.
     * @return the found Value if a match was found, null otherwise.
     */
    public Value findInProtonpack(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return StreamUtils.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, making use of Guava.
     * @return the found Value if a match was found, null otherwise.
     */
    public Value findInGuava(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return Streams.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of {@link Stream} together, using the BiPredicate to let through an element if a match is made
     * or null otherwise, using the zipline approach.
     * @return the found Value if a match was found, null otherwise.
     */
    public Value findInZipline(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        Iterator<Value> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next()) ? t : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Zips two sequences of Kotlin {@link Sequence}s in Java together, using the BiPredicate to let through an element
     * if a match is made or null otherwise
     * @return the found Value if a match was found, null otherwise.
     */
    public Value find(Sequence<Value> q1, Sequence<Value> q2, BiPredicate<Value, Value> predicate) {
        return firstOrNull(
                filter(
                        zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }
}
