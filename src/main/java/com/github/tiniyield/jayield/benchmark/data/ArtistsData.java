package com.github.tiniyield.jayield.benchmark.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jayield.Query;
import org.jooq.lambda.Seq;

import com.github.tiniyield.jayield.benchmark.data.loader.FileLoader;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;

import one.util.streamex.StreamEx;

public class ArtistsData implements ICountryBasedDataProvider<Artist> {
    private final Map<String, Artist[]> data;

    public ArtistsData(CountriesData countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadArtists(name));
        });

    }

    @Override
    public List<Artist> asList(String country) {
        return Arrays.asList(getArtistsForCountry(country));
    }

    @Override
    public Stream<Artist> asStream(String country) {
        return Stream.of(getArtistsForCountry(country));
    }

    @Override
    public StreamEx<Artist> asStreamEx(String country) {
        return StreamEx.of(getArtistsForCountry(country));
    }

    @Override
    public Query<Artist> asQuery(String country) {
        return Query.of(getArtistsForCountry(country));
    }

    @Override
    public Seq<Artist> asSeq(String country) {
        return Seq.of(getArtistsForCountry(country));
    }

    @Override
    public io.vavr.collection.Stream<Artist> asVavrStream(String country) {
        return io.vavr.collection.Stream.of(getArtistsForCountry(country));
    }

    private Artist[] getArtistsForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Artist[0]);
    }

    public boolean hasDataForCountry(String country) {
        return data.containsKey(country) && data.get(country).length > 0;
    }
}
