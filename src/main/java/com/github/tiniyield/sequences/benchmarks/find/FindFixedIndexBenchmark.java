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
 * FindFixedIndexBenchmark
 * The `find` between two sequences is an operation that, based on a user defined
 * predicate, finds two elements that match, returning one of them in the process.
 * <p>
 * For this benchmark the matching element will be on a fixed index, using the
 * following criteria:
 * * For collections with more than 100 elements, the matching index will correspond
 * to COLLECTION_SIZE / 100.
 * (e.g: for a COLLECTION_SIZE of 100K the pipeline will match with the 1000th element)
 * <p>
 * * Otherwise, the matching index will correspond to COLLECTION_SIZE / 10.
 * (e.g: for a COLLECTION_SIZE of 100 elements the pipeline will match with the 10th element)
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
public class FindFixedIndexBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"1000"})
    public int COLLECTION_SIZE;

    /**
     * A list containing Strings initiated with integers from 0 to COLLECTION_SIZE
     */
    public List<String> lstA;
    /**
     * A list containing Strings initiated with getMatchIndex()
     */
    public List<String> lstB;

    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        lstA = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        lstB = IntStream
                .rangeClosed(0, COLLECTION_SIZE)
                .map(v -> getMatchIndex())
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the match index between sequences based on the COLLECTION_SIZE requested
     *
     * @return the match index
     */
    private Integer getMatchIndex() {
        int matchIndex = COLLECTION_SIZE / 100;
        if (COLLECTION_SIZE < 100) {
            matchIndex = COLLECTION_SIZE / 10;
        }
        return matchIndex;
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
