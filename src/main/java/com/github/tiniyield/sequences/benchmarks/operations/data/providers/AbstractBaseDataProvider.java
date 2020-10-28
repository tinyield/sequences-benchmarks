package com.github.tiniyield.sequences.benchmarks.operations.data.providers;

import kotlin.collections.ArraysKt;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractBaseDataProvider<T> implements ISequenceBenchmarkDataProvider<T> {

    protected abstract T[] data();

    @Override
    public List<T> asList() {
        return List.of(data());
    }

    @Override
    public Stream<T> asStream() {
        return Stream.of(data());
    }

    @Override
    public StreamEx<T> asStreamEx() {
        return StreamEx.of(data());
    }

    @Override
    public Query<T> asQuery() {
        return Query.of(data());
    }

    @Override
    public Seq<T> asSeq() {
        return Seq.of(data());
    }

    @Override
    public Sequence<T> asSequence() {
        return ArraysKt.asSequence(data());
    }

    @Override
    public io.vavr.collection.Stream<T> asVavrStream() {
        return io.vavr.collection.Stream.of(data());
    }
}
