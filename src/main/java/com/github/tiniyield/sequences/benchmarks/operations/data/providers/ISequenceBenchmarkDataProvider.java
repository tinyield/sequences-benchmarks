package com.github.tiniyield.sequences.benchmarks.operations.data.providers;

import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.stream.Stream;

public interface ISequenceBenchmarkDataProvider<T> {
    List<T> asList();
    Stream<T> asStream();
    StreamEx<T> asStreamEx();
    Query<T> asQuery();
    Seq<T> asSeq();
    Sequence<T> asSequence();

    io.vavr.collection.Stream<T> asVavrStream();
}
