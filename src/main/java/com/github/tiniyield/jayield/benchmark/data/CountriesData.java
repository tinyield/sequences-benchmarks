package com.github.tiniyield.jayield.benchmark.data;

import java.util.stream.Stream;

import org.jayield.Query;
import org.jooq.lambda.Seq;

import com.github.tiniyield.jayield.benchmark.data.loader.FileLoader;
import com.github.tiniyield.jayield.benchmark.model.country.Country;

import one.util.streamex.StreamEx;

public class CountriesData {

    private final Country[] data;

    public CountriesData() {
        data = new FileLoader().loadCountries().toArray(Country[]::new);
    }

    public Stream<Country> asStream() {
        return Stream.of(data);
    }

    public io.vavr.collection.Stream<Country> asVavrStream() {
        return io.vavr.collection.Stream.of(data);
    }


    public StreamEx<Country> asStreamEx() {
        return StreamEx.of(data);
    }

    public Seq<Country> asSeq() {
        return Seq.of(data);
    }

    public Query<Country> asQuery() {
        return Query.of(data);
    }

}
