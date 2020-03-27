package com.github.tiniyield.sequences.benchmarks.data.providers;

import java.util.HashMap;
import java.util.Map;

import com.github.tiniyield.sequences.benchmarks.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;

public class TracksDataProvider extends AbstractCountryBasedDataProvider<Track> {
    private final Map<String, Track[]> data;

    public TracksDataProvider(CountriesDataProvider countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadTracks(name));
        });
    }

    @Override
    protected Track[] data(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Track[0]);
    }

}
