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
public class EveryStringBenchmark {
    @Param({"100"})
    public int COLLECTION_SIZE;
    /**
     * lstA and lstB are two Lists with the same String  objects.
     */
    private List<String> lstA;
    private List<String> lstB;

    @Setup
    public void init() {
        lstB = new ArrayList<>(COLLECTION_SIZE);
        lstA = IntStream
                .rangeClosed(1, COLLECTION_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        lstB.addAll(lstA);
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
        return every(lstA.stream(), lstB.stream(), String::equals);
    }

    public boolean allEqualStreamEx() {
        return every(StreamEx.of(lstA), StreamEx.of(lstB), String::equals);
    }

    public boolean allEqualQuery() {
        return every(Query.fromList(lstA), Query.fromList(lstB), String::equals);
    }

    public boolean allEqualJool() {
        return every(Seq.seq(lstA), Seq.seq(lstB), String::equals);
    }

    public boolean allEqualVavr() {
        return every(Stream.ofAll(lstA), Stream.ofAll(lstB), String::equals);
    }

    public boolean allEqualProtonpack() {
        return everyProtonpack(lstA.stream(), lstB.stream(), String::equals);
    }

    public boolean allEqualGuava() {
        return everyGuava(lstA.stream(), lstB.stream(), String::equals);
    }

    public boolean allEqualZipline() {
        return everyZipline(lstA.stream(), lstB.stream(), String::equals);
    }

    public boolean allEqualKotlin() {
        return EveryKt.every(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), String::equals);
    }

    public boolean allEqualJKotlin() {
        return every(SequencesKt.asSequence(lstA.iterator()), SequencesKt.asSequence(lstB.iterator()), String::equals);
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
