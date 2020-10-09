package com.github.tiniyield.sequences.benchmarks.operations.data.providers.number;

import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NestedIntegerDataProvider {

    private final int size;

    public NestedIntegerDataProvider(int size) {
        this.size = size;
    }


    public List<List<Integer>> asList() {
        List<List<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(List.of(1));
        }
        return result;
    }


    public Stream<Stream<Integer>> asStream() {
        List<Stream<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Stream.of(1));
        }
        return result.stream();
    }

    public Stream<Stream<Integer>> asParallelStream() {
        List<Stream<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Stream.of(1).parallel());
        }
        return result.stream().parallel();
    }


    public StreamEx<StreamEx<Integer>> asStreamEx() {
        List<StreamEx<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(StreamEx.of(1));
        }
        return StreamEx.of(result);
    }


    public Query<Query<Integer>> asQuery() {
        List<Query<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Query.of(1));
        }
        return Query.fromList(result);
    }


    public Seq<Seq<Integer>> asSeq() {
        List<Seq<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(Seq.of(1));
        }
        return Seq.seq(result);
    }


    public Sequence<Sequence<Integer>> asSequence() {
        List<Sequence<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(SequencesKt.sequenceOf(1));
        }
        return SequencesKt.asSequence(result.iterator());
    }


    public io.vavr.collection.Stream<io.vavr.collection.Stream<Integer>> asVavrStream() {
        List<io.vavr.collection.Stream<Integer>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(io.vavr.collection.Stream.of(1));
        }
        return io.vavr.collection.Stream.ofAll(result);
    }
}
