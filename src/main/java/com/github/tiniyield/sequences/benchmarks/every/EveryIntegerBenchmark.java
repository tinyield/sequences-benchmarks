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

package com.github.tiniyield.sequences.benchmarks.every;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.every.EveryKt;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.zip;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class EveryIntegerBenchmark {
    @Param({"100"})
    public int COLLECTION_SIZE;
    /**
     * lstA and lstB are two Lists with the same Integer objects.
     */
    public List<Integer> lstA;
    public List<Integer> lstB;

    @Setup
    public void init() {
        lstB = new ArrayList<>(COLLECTION_SIZE);
        lstA = IntStream
            .rangeClosed(1, COLLECTION_SIZE)
            .boxed()
            .collect(Collectors.toList());
        lstA.forEach(lstB::add);
    }


    public <T, U> boolean every(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        return zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean every(StreamEx<T> q1, StreamEx<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean every(Query<T> q1, Query<U> q2, BiPredicate<T, U> predicate) {
        return q1.zip(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T,U> boolean everyProtonpack(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T,U> predicate) {
        return StreamUtils.zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean every(Seq<T> q1, Seq<U> q2, BiPredicate<T, U> predicate) {
        return q1.zip(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean every(Stream<T> q1, Stream<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test).forAll(Boolean.TRUE::equals);
    }

    public <T, U> boolean everyGuava(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        return Streams.zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean everyZipline(java.util.stream.Stream<T> q1, java.util.stream.Stream<U> q2, BiPredicate<T, U> predicate) {
        Iterator<U> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next())).allMatch(Boolean.TRUE::equals);
    }

    public <T, U> boolean every(Sequence<T> q1, Sequence<U> q2, BiPredicate<T, U> predicate) {
        return SequencesKt.all(SequencesKt.zip(q1, q2, predicate::test), Boolean.TRUE::equals);
    }

    public boolean allEqualStream() {
        return every(lstA.stream(), lstB.stream(), Integer::equals);
    }

    public boolean allEqualStreamEx() {
        return every(StreamEx.of(lstA), StreamEx.of(lstB), Integer::equals);
    }

    public boolean allEqualQuery() {
        return every(Query.fromList(lstA), Query.fromList(lstB), Integer::equals);
    }

    public boolean allEqualJool() {
        return every(Seq.seq(lstA), Seq.seq(lstB), Integer::equals);
    }

    public boolean allEqualVavr() {
        return every(Stream.ofAll(lstA), Stream.ofAll(lstB), Integer::equals);
    }

    public boolean allEqualProtonpack() {
        return everyProtonpack(lstA.stream(), lstB.stream(), Integer::equals);
    }

    public boolean allEqualGuava() {
        return everyGuava(lstA.stream(), lstB.stream(), Integer::equals);
    }

    public boolean allEqualZipline() {
        return everyZipline(lstA.stream(), lstB.stream(), Integer::equals);
    }

    public boolean allEqualKotlin() {
        return EveryKt.every(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), Integer::equals);
    }

    public boolean allEqualJKotlin() {
        return every(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), Integer::equals);
    }

    @Benchmark
    public final void stream(Blackhole bh) {
        bh.consume(allEqualStream());
    }

    @Benchmark
    public final void streamEx(Blackhole bh) {
        bh.consume(allEqualStreamEx());
    }

    @Benchmark
    public final void jayield(Blackhole bh) {
        bh.consume(allEqualQuery());
    }

    @Benchmark
    public final void jool(Blackhole bh) {
        bh.consume(allEqualJool());
    }

    @Benchmark
    public final void vavr(Blackhole bh) {
        bh.consume(allEqualVavr());
    }

    @Benchmark
    public final void protonpack(Blackhole bh) {
        bh.consume(allEqualProtonpack());
    }

    @Benchmark
    public final void guava(Blackhole bh) {
        bh.consume(allEqualGuava());
    }

    @Benchmark
    public final void zipline(Blackhole bh) {
        bh.consume(allEqualZipline());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(allEqualKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(allEqualJKotlin());
    }

}
