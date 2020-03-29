package com.github.tiniyield.sequences.benchmarks.data.providers;

import java.util.List;
import java.util.stream.Stream;

import org.jayield.Query;
import org.jooq.lambda.Seq;

import com.github.tiniyield.sequences.benchmarks.model.country.Country;

import one.util.streamex.StreamEx;

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
    public io.vavr.collection.Stream<T> asVavrStream() {
        return io.vavr.collection.Stream.of(data());
    }
}
