package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.github.tiniyield.sequences.benchmarks.kt.flatmap.FlatmapAndReduceKt;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class FlatMapAndReduceBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;
    private List<List<Integer>> data;

    public <T,U> T getNestedSequence(Function<List<Integer>, U> innerMapper, Function<List<U>, T> outerMapper) {
        List<U> result = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            result.add(innerMapper.apply(data.get(i)));
        }
        return outerMapper.apply(result);
    }

    public List<List<Integer>> getNestedList() {
        List<List<Integer>> result = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            result.add(List.of(1));
        }
        return result;
    }

    public <T> Sequence<T> toKotlinSequence(Collection<T> source){
        return SequencesKt.asSequence(source.iterator());
    }

    public Integer flatMapAndReduceStream(Stream<Stream<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    public Integer flatMapAndReduceStreamEx(StreamEx<StreamEx<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    public Integer flatMapAndReduceQuery(Query<Query<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    public Integer flatMapAndReduceJool(Seq<Seq<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }

    public Integer flatMapAndReduceVavr(io.vavr.collection.Stream<io.vavr.collection.Stream<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum);
    }

    public Integer flatMapAndReduceJKotlin(Sequence<Sequence<Integer>> input) {
        return SequencesKt.reduce(
                SequencesKt.flatMap(
                        input,
                        i -> i
                ),
                Integer::sum
        );
    }

    public Integer sumStream() {
        return flatMapAndReduceStream(getNestedSequence(List::stream, List::stream));
    }

    public Integer sumStreamEx() {
        return flatMapAndReduceStreamEx(getNestedSequence(StreamEx::of, StreamEx::of));
    }

    public Integer sumQuery() {
        return flatMapAndReduceQuery(getNestedSequence(l -> Query.of(l.toArray(Integer[]::new)), Query::fromList));
    }

    public Integer sumJool() {
        return flatMapAndReduceJool(getNestedSequence(Seq::seq, Seq::seq));
    }

    public Integer sumVavr() {
        return flatMapAndReduceVavr(getNestedSequence(io.vavr.collection.Stream::ofAll, io.vavr.collection.Stream::ofAll));
    }

    public Integer sumKotlin() {
        return FlatmapAndReduceKt.flatMapAndReduce(getNestedSequence(this::toKotlinSequence, this::toKotlinSequence));
    }

    public Integer sumJKotlin() {
        return flatMapAndReduceJKotlin(getNestedSequence(this::toKotlinSequence, this::toKotlinSequence));
    }

    @Setup
    public void setup() {
        data = getNestedList();
    }

    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(sumStream());
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(sumStreamEx());
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(sumQuery());
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(sumJool());
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(sumVavr());
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(sumKotlin());
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(sumJKotlin());
    }
}
