package com.github.tiniyield.jayield.benchmark.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jayield.Query;

import com.github.tiniyield.jayield.benchmark.data.loader.FileLoader;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;

public class ArtistsData implements ICountryBasedDataProvider<Artist> {
    private final Map<String, Artist[]> data;

    public ArtistsData(CountriesData countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadArtists(name));
        });

    }

    public List<Artist> asList(String country) {
        return Arrays.asList(getArtistsForCountry(country));
    }

    public Stream<Artist> asStream(String country) {
        return Stream.of(getArtistsForCountry(country));
    }

    public Query<Artist> asQuery(String country) {
        return Query.of(getArtistsForCountry(country));
    }

    private Artist[] getArtistsForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Artist[0]);
    }

    public boolean hasDataForCountry(String country) {
        return data.containsKey(country) && data.get(country).length > 0;
    }
}
