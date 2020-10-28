package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.github.tiniyield.sequences.benchmarks.AbstractSequenceOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.NestedIntegerDataProvider;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;
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
public class FlatMapAndReduceBenchmark extends AbstractSequenceOperationsBenchmark {

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

    @Setup
    public void setup() {
        super.init();
        data = getNestedList();
    }

    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(stream.flatMapAndReduce(getNestedSequence(List::stream, List::stream)));
    }

    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(streamEx.flatMapAndReduce(getNestedSequence(StreamEx::of, StreamEx::of)));
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(query.flatMapAndReduce(getNestedSequence(Query::fromList, Query::fromList)));
    }

    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(jool.flatMapAndReduce(getNestedSequence(Seq::seq, Seq::seq)));
    }

    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(vavr.flatMapAndReduce(getNestedSequence(io.vavr.collection.Stream::ofAll, io.vavr.collection.Stream::ofAll)));
    }

    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(kotlin.flatMapAndReduce(getNestedSequence(this::toKotlinSequence, this::toKotlinSequence)));
    }

    @Benchmark
    public void jkotlin(Blackhole bh) {
        bh.consume(jkotlin.flatMapAndReduce(getNestedSequence(this::toKotlinSequence, this::toKotlinSequence)));
    }
}
