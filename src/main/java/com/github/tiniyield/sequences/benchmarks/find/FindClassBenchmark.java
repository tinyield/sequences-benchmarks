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
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.google.common.collect.Streams;
import io.vavr.collection.Stream;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
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

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.zip;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindClassBenchmark {

    @Param({"1000"})
    public int COLLECTION_SIZE;
    public int index;
    private List<Value> lstA;
    private List<Value> lstB;

    @Setup
    public void init() {
        index = 0;
        lstA = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(Value::new)
                .collect(Collectors.toList());
        lstB = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(v -> -1)
                .map(Value::new)
                .collect(Collectors.toList());
    }

    @Setup(Level.Invocation)
    public void update() {
        index++;
        lstB.set((index - 1) % COLLECTION_SIZE, new Value(-1));
        lstB.set(index % COLLECTION_SIZE, new Value(index % COLLECTION_SIZE));
    }

    public Value find(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value find(StreamEx<Value> q1, StreamEx<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value find(Query<Value> q1, Query<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value find(Seq<Value> q1, Seq<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value find(Stream<Value> q1, Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull).getOrNull();
    }

    public Value findInProtonpack(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return StreamUtils.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    public Value findInGuava(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        return Streams.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value findInZipline(java.util.stream.Stream<Value> q1, java.util.stream.Stream<Value> q2, BiPredicate<Value, Value> predicate) {
        Iterator<Value> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next()) ? t : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Value find(Sequence<Value> q1, Sequence<Value> q2, BiPredicate<Value, Value> predicate) {
        return SequencesKt.firstOrNull(
                SequencesKt.filter(
                        SequencesKt.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }

    public Value findEqualInStream() {
        return find(lstA.stream(), lstB.stream(), Value::equals);
    }

    public Value findEqualInStreamEx() {
        return find(StreamEx.of(lstA), StreamEx.of(lstB), Value::equals);
    }

    public Value findEqualInQuery() {
        return find(Query.fromList(lstA), Query.fromList(lstB), Value::equals);
    }

    public Value findEqualInJool() {
        return find(Seq.seq(lstA), Seq.seq(lstB), Value::equals);
    }

    public Value findEqualInVavr() {
        return find(Stream.ofAll(lstA), Stream.ofAll(lstB), Value::equals);
    }

    public Value findEqualInProtonpack() {
        return findInProtonpack(lstA.stream(), lstB.stream(), Value::equals);
    }

    public Value findEqualInGuava() {
        return findInGuava(lstA.stream(), lstB.stream(), Value::equals);
    }

    public Value findEqualInZipline() {
        return findInZipline(lstA.stream(), lstB.stream(), Value::equals);
    }

    public Value findEqualInKotlin() {
        return FindKt.find(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), Value::equals);
    }

    public Value findEqualInJKotlin() {
        return find(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), Value::equals);
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
