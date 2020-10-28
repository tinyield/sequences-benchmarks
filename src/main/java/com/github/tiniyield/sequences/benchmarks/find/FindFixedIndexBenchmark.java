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
import com.github.tiniyield.sequences.benchmarks.kt.FindKt;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.zip;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindFixedIndexBenchmark {

    @Param({"1000"})
    public int COLLECTION_SIZE;
    private List<String> lstA;
    private List<String> lstB;


    @Setup
    public void init() {
        lstA = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        lstB = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(v -> getMatchIndex())
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private Integer getMatchIndex() {
        int matchIndex = COLLECTION_SIZE / 100;
        if (COLLECTION_SIZE < 100) {
            matchIndex = COLLECTION_SIZE / 10;
        }
        return matchIndex;
    }


    public String find(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String find(StreamEx<String> q1, StreamEx<String> q2, BiPredicate<String, String> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String find(Query<String> q1, Query<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String find(Seq<String> q1, Seq<String> q2, BiPredicate<String, String> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String find(Stream<String> q1, Stream<String> q2, BiPredicate<String, String> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull).getOrNull();
    }

    public String findInProtonpack(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return StreamUtils.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    public String findInGuava(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        return Streams.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String findInZipline(java.util.stream.Stream<String> q1, java.util.stream.Stream<String> q2, BiPredicate<String, String> predicate) {
        Iterator<String> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next()) ? t : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String find(Sequence<String> q1, Sequence<String> q2, BiPredicate<String, String> predicate) {
        return SequencesKt.firstOrNull(
                SequencesKt.filter(
                        SequencesKt.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }

    public String findEqualInStream() {
        return find(lstA.stream(), lstB.stream(), String::equals);
    }

    public String findEqualInStreamEx() {
        return find(StreamEx.of(lstA), StreamEx.of(lstB), String::equals);
    }

    public String findEqualInQuery() {
        return find(Query.fromList(lstA), Query.fromList(lstB), String::equals);
    }

    public String findEqualInJool() {
        return find(Seq.seq(lstA), Seq.seq(lstB), String::equals);
    }

    public String findEqualInVavr() {
        return find(Stream.ofAll(lstA), Stream.ofAll(lstB), String::equals);
    }

    public String findEqualInProtonpack() {
        return findInProtonpack(lstA.stream(), lstB.stream(), String::equals);
    }

    public String findEqualInGuava() {
        return findInGuava(lstA.stream(), lstB.stream(), String::equals);
    }

    public String findEqualInZipline() {
        return findInZipline(lstA.stream(), lstB.stream(), String::equals);
    }

    public String findEqualInKotlin() {
        return FindKt.find(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), String::equals);
    }

    public String findEqualInJKotlin() {
        return find(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), String::equals);
    }

    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(findEqualInStream());
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(findEqualInStreamEx());
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(findEqualInQuery());
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(findEqualInJool());
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(findEqualInVavr());
    }

    @Benchmark
    public void protonpack(Blackhole bh) {
        bh.consume(findEqualInProtonpack());
    }

    @Benchmark
    public void guava(Blackhole bh) {
        bh.consume(findEqualInGuava());
    }

    @Benchmark
    public void zipline(Blackhole bh) {
        bh.consume(findEqualInZipline());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(findEqualInKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(findEqualInJKotlin());
    }

}
