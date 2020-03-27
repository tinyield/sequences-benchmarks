package com.github.tiniyield.sequences.benchmarks.data.providers;

import java.util.HashMap;
import java.util.Map;

import com.github.tiniyield.sequences.benchmarks.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;

public class ArtistsDataProvider extends AbstractCountryBasedDataProvider<Artist> {
    private final Map<String, Artist[]> data;

    public ArtistsDataProvider(CountriesDataProvider countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadArtists(name));
        });

    }

    @Override
    protected Artist[] data(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Artist[0]);
    }
}
