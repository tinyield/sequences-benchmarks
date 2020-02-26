package com.github.tiniyield.jayield.benchmark.data;

import java.util.List;
import java.util.stream.Stream;

import org.jayield.Query;

import one.util.streamex.StreamEx;

public interface ICountryBasedDataProvider<T> {

    List<T> asList(String country);
    Stream<T> asStream(String country);

    StreamEx<T> asStreamEx(String country);

    Query<T> asQuery(String country);
    boolean hasDataForCountry(String country);
}
