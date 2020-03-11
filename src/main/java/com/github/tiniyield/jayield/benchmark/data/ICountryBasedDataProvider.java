package com.github.tiniyield.jayield.benchmark.data;

import java.util.List;
import java.util.stream.Stream;

import org.jayield.Query;
import org.jooq.lambda.Seq;

import one.util.streamex.StreamEx;

public interface ICountryBasedDataProvider<T> {

    boolean hasDataForCountry(String country);

    List<T> asList(String country);
    Stream<T> asStream(String country);
    StreamEx<T> asStreamEx(String country);
    Query<T> asQuery(String country);
    Seq<T> asSeq(String country);
    io.vavr.collection.Stream<T> asVavrStream(String country);
}
